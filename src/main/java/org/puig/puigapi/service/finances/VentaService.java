package org.puig.puigapi.service.finances;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.VentaInvalidaException;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.persistence.entity.utils.DetalleDe;
import org.puig.puigapi.persistence.repositories.finances.VentaRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.puig.puigapi.service.operation.SucursalService;
import org.puig.puigapi.service.operation.UsuarioService;
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
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Setter(onMethod_ = @Autowired)
@PuigService(Venta.class)
public class VentaService extends PersistenceService<Venta, String, VentaRepository> {

    protected ArticuloMenuService articuloMenuService;

    protected EmpleadoService empleadoService;
    protected SucursalService sucursalService;
    protected UsuarioService usuarioService;

    protected MongoTemplate template;

    public VentaService(VentaRepository repository) {
        super(repository);
    }

    public List<Venta> readByFecha_venta(@NotNull LocalDate desde, @NotNull LocalDate hasta) {
        var start = desde.atStartOfDay();
        var end = hasta.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end);
    }

    public List<Venta> readByFecha_venta(@NotNull LocalDate fecha) {
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
        asignarPrecioDetalle(venta);

        //Obtener el puesto del empleado
        Empleado asignada_a = empleadoService.readByID(venta.getTomada_por());
        venta.getTomada_por().setPuesto(asignada_a.getPuesto());

        if (!venta.esValida())
            throw VentaInvalidaException.pagoInferiorAMonto(venta);

        venta.setId(generarId(venta.getRealizada_en()).toString());
        Venta saVenta = super.save(venta);
        Sucursal sucursal = sucursalService.readByID(venta.getRealizada_en());
        asignarPorReceta(venta, sucursal::quitarExistencias);

        if (!sucursalService.update(sucursal))
            throw new MongoTransactionException("Error durante la actualización de bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return saVenta;
    }

    @Transactional
    public Venta.Reparto save(@NotNull Venta.Reparto reparto) {
        Usuario cliente = usuarioService.saveOrReadById(reparto.getCliente_reparto());
        reparto.setCliente_reparto(cliente);

        return (Venta.Reparto) save((Venta) reparto);
    }

    @Override
    @Transactional
    public boolean delete(@NotNull String id) {
        Venta venta = readByID(id);
        Sucursal sucursal = sucursalService.readByID(venta.getRealizada_en());
        asignarPorReceta(venta, sucursal::agregarExistencias);

        boolean res = super.delete(id);
        if (!sucursalService.update(sucursal) && res)
            throw new MongoTransactionException("Error durante la actualización de bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return res;
    }

    public boolean asignarVenta(@NotNull Venta venta, @NotNull Empleado empleado) {
        Venta reVenta = readByID(venta);
        Empleado reEmpleado = empleadoService.readByID(empleado);
        if (reEmpleado.getPuesto() != Empleado.Puestos.CAJERO)
            throw VentaInvalidaException.empleadoNoEsCajero(empleado);

        reVenta.setAsignada_a(reEmpleado);
        return update(reVenta);
    }

    public List<Venta.ReporteProducto> generarReporteProducto(@NotNull LocalDate desde,
                                                              @NotNull LocalDate hasta) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("fecha_venta").gte(desde).lte(hasta)),
                Aggregation.unwind("detalle"),
                LookupOperation.newLookup()
                        .from("finances")
                        .localField("detalle.objeto.$id")
                        .foreignField("_id")
                        .as("articulo"),
                Aggregation.project()
                        .andExpression("articulo").arrayElementAt(0).as("articulo")
                        .andExpression("detalle.cantidad").as("cantidad_total")
                        .andExpression("detalle.monto").as("monto_total"),
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
                                            @NotNull Venta.FormasEntrega filtro) {
        var start = desde.atStartOfDay();
        var end = hasta.atStartOfDay().plusDays(1).minusSeconds(1);

        return repository.findByFecha(start, end, filtro);
    }

    protected void asignarPrecioDetalle(@NotNull Venta venta) {
        venta.getDetalle().forEach(d -> {
            ArticuloMenu articuloMenu = articuloMenuService.readByID(d.getObjeto());
            d.setObjeto(articuloMenu); //save precio and monto
        });
    }

    protected void asignarPorReceta(@NotNull Venta venta,
                                    @NotNull Consumer<ArticuloMenu.Porcion> consumer) {
        venta.getDetalle().forEach(d -> {
            switch (d.getObjeto().getEspecializado()) {
                case ARTICULO_MENU -> ((ArticuloMenu) d.getObjeto())
                        .getReceta()
                        .stream()
                        .peek(r -> r.per(d.getCantidad()))
                        .forEach(consumer);
                case COMBO -> ((Combo) d.getObjeto())
                        .getContenido()
                        .stream()
                        .map(DetalleDe::getObjeto)
                        .map(ArticuloMenu::getReceta)
                        .flatMap(Collection::stream)
                        .peek(r -> r.per(d.getCantidad()))
                        .forEach(consumer);
            }
        });
        venta.update();
    }

    @Transactional
    protected StringBuilder generarId(@NotNull Sucursal sucursal) {
        String sucursal_id = sucursal.getId();
        String fecha_venta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = count(Venta.class, Venta.Reparto.class);

        return new StringBuilder()
                .append(sucursal_id).append("_")
                .append(fecha_venta).append("_")
                .append(seq);
    }
}
