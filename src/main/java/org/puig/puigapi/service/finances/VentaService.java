package org.puig.puigapi.service.finances;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.CreacionVentaException;
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
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

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

    /*@Override
    @Transactional
    public Venta save(@NotNull Venta venta) {
        asignarPrecioDetalle(venta);
        venta.setMonto_total(venta.getMonto_total());
        venta.setPago_total(venta.getPago_total());

        //Obtener el puesto del empleado
        Empleado asignada_a = empleadoService.readByID(venta.getAsignada_a().getId());
        venta.getAsignada_a().setPuesto(asignada_a.getPuesto());

        if (!venta.esValida())
            throw CreacionVentaException.pagoInferiorAMonto(venta);

        Mono<Venta> saveVenta = reactiveTemplate.save(venta);
        Sucursal sucursal = sucursalService.readByID(venta.getRealizada_en());
        asignarPorReceta(venta, sucursal::quitarExistencias);

        Query query = new Query(Criteria.where("_id").is(sucursal.getId()));
        Update update = new Update().set("bodega", sucursal.getBodega());
        Mono<UpdateResult> upSucursal = reactiveTemplate.updateFirst(query, update, Sucursal.class);
        saveVenta
                .then(upSucursal)
                .then()
                .as(transactionOperator::transactional);

        return saveVenta.block();
    }*/

    @Override
    public List<Venta> readAll() {
        return repository.findAllVentas();
    }

    /*@Override
    @Transactional
    public boolean delete(@NotNull String id) {
        Venta venta = readByID(id);
        Sucursal sucursal = sucursalService.readByID(venta.getRealizada_en());
        asignarPorReceta(venta, sucursal::agregarExistencias);

        Query query = new Query(Criteria.where("_id").is(sucursal.getId()));
        Update update = new Update().set("bodega", sucursal.getBodega());
        Mono<UpdateResult> upSucursal = reactiveTemplate.updateFirst(query, update, Sucursal.class);
        Mono<DeleteResult> deSucursal = reactiveTemplate.remove(venta);

        upSucursal
                .then(deSucursal)
                .then()
                .as(transactionOperator::transactional);

        return true;
    }*/

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
    }
}
