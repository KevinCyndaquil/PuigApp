package org.puig.puigapi.service.finances;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.VentaInvalidaException;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.util.Articulo;
import org.puig.puigapi.persistence.repository.finances.VentaRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.puig.puigapi.service.operation.SucursalService;
import org.puig.puigapi.service.operation.UsuarioService;
import org.puig.puigapi.util.contable.Calculable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Setter(onMethod_ = @Autowired)
@PuigService(Venta.class)
public class VentaService extends PersistenceService<Venta, String, VentaRepository> {

    protected ArticuloMenuService articuloMenuService;
    protected ArticuloService articuloService;

    protected EmpleadoService empleadoService;
    protected SucursalService sucursalService;
    protected UsuarioService usuarioService;

    protected MongoTemplate template;

    public VentaService(VentaRepository repository) {
        super(repository);
    }

    public List<Venta> readByPeriodo(@NotNull LocalDate desde, @NotNull LocalDate hasta) {
        var start = desde.atStartOfDay();
        var end = hasta.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end);
    }

    public List<Venta> readByPeriodo(@NotNull LocalDate fecha) {
        var start = fecha.atStartOfDay();
        var end = fecha.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end);
    }

    @Override
    public List<Venta> readAllWhile() {
        return super.readAllWhile(Venta.class, Venta.Reparto.class);
    }

    @Override
    @Transactional
    public Venta save(@NotNull Venta venta) {
        venta.getTicket().forEach(d -> {
            Articulo articulo = articuloService.readById(d.getDetalle());
            d.setDetalle(articulo);
            System.out.println(articulo.getClass());
        });

        //Obtener el puesto del empleado
        Empleado tomadaPor = empleadoService.readById(venta.getTomada_por());
        venta.setTomada_por(tomadaPor);

        venta.validar();
        venta.update();
        venta.setId(generarId(venta.getRealizada_en()).toString());

        Venta ventaRealizada = super.save(venta);
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

    @Transactional
    public Venta.Reparto save(@NotNull Venta.Reparto reparto) {
        Usuario cliente = usuarioService.saveOrReadById(reparto.getCliente_reparto());
        if (cliente.getDirecciones().add(reparto.getDireccion_reparto()))
            usuarioService.update(cliente);

        reparto.setCliente_reparto(cliente);

        return (Venta.Reparto) save((Venta) reparto);
    }

    @Override
    @Transactional
    public boolean delete(@NotNull String id) {
        Venta venta = readById(id);
        venta.getTicket().stream().map(Calculable::getDetalle)
                .forEach(System.out::println);
        Sucursal sucursal = sucursalService.readById(venta.getRealizada_en());
        venta.forEachDetalle(sucursal.getBodega()::agregarDevolucion);

        boolean res = super.delete(id);
        if (!sucursalService.update(sucursal) && res)
            throw new MongoTransactionException("Error durante la actualización de bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return res;
    }

    public boolean asignarVenta(@NotNull Venta venta, @NotNull Empleado empleado) {
        Venta reVenta = readById(venta);
        Empleado reEmpleado = empleadoService.readById(empleado);
        if (reEmpleado.getPuesto() != Empleado.Puestos.CAJERO)
            throw VentaInvalidaException.empleadoNoEsCajero(empleado);

        reVenta.setAsignada_a(reEmpleado);
        return update(reVenta);
    }

    public List<Venta.ReporteProducto> generarReporteProducto(@NotNull LocalDate desde,
                                                              @NotNull LocalDate hasta) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("fecha_venta").gte(desde).lte(hasta)),
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
        return results.getMappedResults();
    }

    public List<Venta> generarReporteVentas(@NotNull LocalDate desde,
                                            @NotNull LocalDate hasta,
                                            @NotNull Venta.ModosDeEntrega filtro) {
        var start = desde.atStartOfDay();
        var end = hasta.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end, filtro);
    }

    @Transactional
    protected StringBuilder generarId(@NotNull Sucursal sucursal) {
        String sucursal_id = sucursal.getId();
        String fecha_venta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = count(Venta.class, Venta.Reparto.class);

        return new StringBuilder()
                .append(sucursal_id)
                .append(fecha_venta)
                .append(seq);
    }
}
