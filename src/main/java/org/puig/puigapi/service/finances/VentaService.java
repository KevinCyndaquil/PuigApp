package org.puig.puigapi.service.finances;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.VentaInvalidaException;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.finances.VentaRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Setter(onMethod_ = @Autowired)
@PuigService(Venta.class)
public class VentaService extends PersistenceService<Venta, String, VentaRepository> {

    protected ArticuloMenuService articuloMenuService;

    protected EmpleadoService empleadoService;
    protected SucursalService sucursalService;

    public VentaService(VentaRepository repository) {
        super(repository);
    }

    public List<Venta> readByFecha_venta(@NotNull LocalDate desde, @NotNull LocalDate hasta) {
        return repository.findByFecha(desde, hasta);
    }

    public List<Venta> readByFecha_venta(@NotNull LocalDate fecha) {
        return repository.findByFecha(fecha);
    }

    @Override
    public List<Venta> readAllWhile() {
        return super.readAllWhile(List.of(Venta.class, Venta.Reparto.class));
    }

    @Override
    @Transactional
    public Venta save(@NotNull Venta venta) {
        asignarPrecioDetalle(venta);

        //Obtener el puesto del empleado
        Empleado asignada_a = empleadoService.readByID(venta.getAsignada_a());
        venta.getAsignada_a().setPuesto(asignada_a.getPuesto());

        if (!venta.esValida())
            throw VentaInvalidaException.pagoInferiorAMonto(venta);

        Venta saveVenta = super.save(venta);
        Sucursal sucursal = sucursalService.readByID(venta.getRealizada_en());
        asignarPorReceta(venta, sucursal::quitarExistencias);

        if (!sucursalService.update(sucursal))
            throw new MongoTransactionException("Error durante la actualización de bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return saveVenta;
    }

    @Override
    @Transactional
    public boolean delete(@NotNull String id) {
        Venta venta = readByID(id);
        Sucursal sucursal = sucursalService.readByID(venta.getRealizada_en());
        asignarPorReceta(venta, sucursal::agregarExistencias);

        boolean res = super.delete(id);
        if (!sucursalService.update(sucursal))
            throw new MongoTransactionException("Error durante la actualización de bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));

        return res;
    }

    protected void asignarPrecioDetalle(@NotNull Venta venta) {
        venta.getDetalle().forEach(d -> {
            String id = d.getObjeto().getId();
            ArticuloMenu articuloMenu = articuloMenuService.readPrecioById(id);
            d.setObjeto(articuloMenu); //save precio and monto
        });
    }

    protected void asignarPorReceta(@NotNull Venta venta,
                                    @NotNull Consumer<ArticuloMenu.Receta> consumer) {
        venta.getDetalle().forEach(d -> {
            switch (d.getObjeto().getEspecializado()) {
                case ARTICULO_MENU -> ((ArticuloMenu) d.getObjeto())
                        .getReceta()
                        .forEach(consumer);
                case COMBO -> ((Combo) d.getObjeto())
                        .getContenido()
                        .stream()
                        .map(ArticuloMenu::getReceta)
                        .flatMap(Collection::stream)
                        .forEach(consumer);
            }
        });
        venta.update();
    }
}
