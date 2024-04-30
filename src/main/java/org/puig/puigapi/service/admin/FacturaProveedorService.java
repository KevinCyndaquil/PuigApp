package org.puig.puigapi.service.admin;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repository.admin.FacturaProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.puig.puigapi.persistence.entity.admin.Proveedor.*;

@PuigService(Factura.class)
@Setter(onMethod_ = @Autowired)
public class FacturaProveedorService extends
        PersistenceService<Factura, String, FacturaProveedorRepository>  {
    protected ProductoProveedorService productoProveedorService;

    protected SucursalService sucursalService;

    public FacturaProveedorService(FacturaProveedorRepository repository) {
        super(repository);
    }

    public List<Factura> readByDate(@NotNull LocalDate fecha) {
        var start = fecha.atStartOfDay();
        var end = fecha.atStartOfDay().plusDays(1).minusSeconds(1);
        return repository.findByFecha(start, end);
    }

    @Override
    @Transactional
    public Factura save(@NotNull Factura factura) throws LlaveDuplicadaException {
        Sucursal sucursal = sucursalService.readById(factura.getSucursal().getId());

        factura.getDetalle().stream()
                .peek(d -> {
                    ObjectId producto_id = d.getDetalle().getId();
                    d.setDetalle(productoProveedorService.readById(producto_id));
                })
                .forEach(sucursal.getBodega()::recepcionar);
        factura.update();

        Factura saFactura = super.save(factura);
        if (!sucursalService.update(sucursal))
            throw new MongoTransactionException("Error al actualizar la bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return saFactura;
    }

    @Override
    @Transactional
    public boolean delete(@NotNull String id) {
        Factura factura = readById(id);
        Sucursal sucursal = sucursalService.readById(factura.getSucursal());

        factura.getDetalle().stream()
                .peek(d -> {
                    ObjectId producto_id = d.getDetalle().getId();
                    d.setDetalle(productoProveedorService.readById(producto_id));
                })
                .forEach(sucursal.getBodega()::quitarExistencias);

        boolean res = super.delete(id);

        if (res)
            if (!sucursalService.update(sucursal))
                throw new MongoTransactionException("Error al actualizar la bodega de sucursal %s"
                        .formatted(sucursal.getNombre()));
        return res;
    }
}
