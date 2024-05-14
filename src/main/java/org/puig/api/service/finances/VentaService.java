package org.puig.api.service.finances;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.bson.types.ObjectId;
import org.puig.api.util.errors.VentaInvalidaException;
import org.puig.api.util.errors.VentaNoAsignadaException;
import org.puig.api.persistence.entity.finances.Venta;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.persistence.entity.operation.Usuario;
import org.puig.api.service.PdfReportService;
import org.puig.api.util.Articulo;
import org.puig.api.persistence.repository.finances.VentaRepository;
import org.puig.api.service.PersistenceService;
import org.puig.api.util.Images;
import org.puig.api.util.Reports;
import org.puig.api.service.operation.EmpleadoService;
import org.puig.api.service.operation.SucursalService;
import org.puig.api.service.operation.UsuarioService;
import org.puig.api.util.contable.Calculable;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VentaService implements PersistenceService<Venta>, PdfReportService {
    final VentaRepository repository;
    final ArticuloMenuService articuloMenuService;
    final ArticuloService articuloService;

    final EmpleadoService empleadoService;
    final SucursalService sucursalService;
    final UsuarioService usuarioService;

    final MongoTemplate template;

    @Override
    public @NonNull VentaRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Venta> clazz() {
        return Venta.class;
    }

    public List<Venta> readByPeriodo(@NonNull LocalDate desde, @NonNull LocalDate hasta) {
        var start = desde.atStartOfDay();
        var end = hasta.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end);
    }

    public List<Venta> readByPeriodo(@NonNull LocalDate fecha) {
        var start = fecha.atStartOfDay();
        var end = fecha.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end);
    }

    @Override
    public List<Venta> readAllWhile() {
        return PersistenceService.super.readAllWhile(Venta.class);
    }

    @Override
    @Transactional
    public Venta save(@NonNull Venta venta) {
        if (venta.getModo_entrega() == Venta.ModosDeEntrega.REPARTO) {
            final Usuario clienteReparto = usuarioService.saveOrGetAsCliente(venta.getCliente_reparto());
            if (clienteReparto.getDirecciones().add(venta.getDireccion_reparto()))
                usuarioService.update(clienteReparto);

            venta.setCliente_reparto(clienteReparto);
        }

        venta.getTicket().forEach(d -> {
            Articulo articulo = articuloService.readById(d.getDetalle());
            d.setDetalle(articulo);
            System.out.println(articulo.getClass());
        });

        //Obtener el puesto del empleado
        Empleado tomadaPor = empleadoService.readById(venta.getTomada_por());
        venta.setTomada_por(tomadaPor);

        venta.validar();
        venta.setFolio(generarFolio(venta.getRealizada_en()).toString());

        Venta ventaRealizada = PersistenceService.super.save(venta);
        Sucursal sucursal = sucursalService.readById(venta.getRealizada_en());
        ventaRealizada.forEachDetalle(sucursal.getBodega()::quitarExistencias);

        if (!sucursalService.update(sucursal))
            throw new MongoTransactionException(
                    "Error durante la actualización de bodega de sucursal %s"
                            .formatted(sucursal.getNombre()));

        ventaRealizada.getTicket().forEach(d -> d.getDetalle().isEn_desabasto(sucursal));

        System.out.println(ventaRealizada);
        return ventaRealizada;
    }

    @Override
    @Transactional
    public boolean delete(@NonNull ObjectId id) {
        Venta venta = readById(id);
        venta.getTicket().stream().map(Calculable::getDetalle)
                .forEach(System.out::println);
        Sucursal sucursal = sucursalService.readById(venta.getRealizada_en());
        venta.forEachDetalle(sucursal.getBodega()::agregarDevolucion);

        boolean res = PersistenceService.super.delete(id);
        if (!sucursalService.update(sucursal) && res)
            throw new MongoTransactionException("Error durante la actualización de bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return res;
    }

    public void asignar(@NonNull Venta venta, @NonNull Empleado empleado) {
        Venta reVenta = readById(venta);
        Empleado reEmpleado = empleadoService.readById(empleado);
        if (reEmpleado.getPuesto() != Empleado.Puestos.CAJERO)
            throw new VentaInvalidaException.EmpleadoInvalido(empleado, Empleado.Puestos.CAJERO);

        reVenta.setAsignada_a(reEmpleado);
        if(!update(reVenta))
            throw new VentaNoAsignadaException(reVenta, reEmpleado);
    }

    public JasperPrint generarReporteVentasProducto(
            @NonNull SimpleInstance sucursalInstance,
            @NonNull LocalDate desde,
            @NonNull LocalDate hasta) throws JRException {
        Sucursal sucursal = sucursalService.readById(sucursalInstance);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("fecha_venta").gte(desde).lte(hasta)
                                .and("realizada_en.$id").is(sucursal.getId())
                                .and("_class").is(Venta.class.getName())),
                Aggregation.unwind("ticket"),
                LookupOperation.newLookup()
                        .from("finances")
                        .localField("ticket.detalle.$id")
                        .foreignField("_id")
                        .as("articulo"),
                Aggregation.project()
                        .andExpression("articulo").arrayElementAt(0).as("articulo")
                        .andExpression("ticket.cantidad").as("cantidad_total")
                        .andExpression("ticket.monto").as("monto_total"),
                Aggregation.group("articulo")
                        .first("articulo").as("articulo")
                        .sum("cantidad_total").as("cantidad_total")
                        .sum("monto_total").as("monto_total")
        );

        AggregationResults<Venta.ReporteProducto> results =
                template.aggregate(aggregation, "finances", Venta.ReporteProducto.class);
        List<Venta.ReporteProducto> data = results.getMappedResults();

        System.out.println(data);

        Map<String, Object> parametros = new HashMap<>();

        try (InputStream lpp = Images.LOGO_POLLOSPUIG.url.openStream();
             InputStream lpa = Images.LOGO_PUIGAPP.url.openStream()) {

            final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(
                    "dd 'de' MMMM 'del' yyyy",
                    new Locale("es", "ES"));

            parametros.put("logo_pollospuig", lpp);
            parametros.put("logo_puigapp", lpa);
            parametros.put("sucursal", sucursal.getNombre());
            parametros.put("fecha_reporte", LocalDate.now().format(dateFormat));
            parametros.put("fecha_inicio", desde.format(dateFormat));
            parametros.put("fecha_fin", hasta.format(dateFormat));
            parametros.put("contenido_ventas", new JRBeanCollectionDataSource(data));

            return JasperFillManager.fillReport(
                    Reports.VENTAS_ARTICULOS.content,
                    parametros,
                    new JREmptyDataSource(1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Venta> generarReporteVentas(@NonNull LocalDate desde,
                                            @NonNull LocalDate hasta,
                                            @NonNull Venta.ModosDeEntrega filtro) {
        var start = desde.atStartOfDay();
        var end = hasta.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end, filtro);
    }

    @Transactional
    protected StringBuilder generarFolio(@NonNull Sucursal sucursal) {
        ObjectId sucursal_id = sucursal.getId();
        String fecha_venta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = count(Venta.class);

        return new StringBuilder()
                .append(sucursal_id)
                .append(fecha_venta)
                .append(seq);
    }
}
