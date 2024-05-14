package org.puig.api.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.puig.api.util.errors.LlaveDuplicadaException;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.persistence.repository.admin.FacturaProveedorRepository;
import org.puig.api.service.PersistenceService;
import org.puig.api.service.operation.SucursalService;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.puig.api.persistence.entity.admin.Proveedor.*;

@Service
@RequiredArgsConstructor
public class FacturaProveedorService implements PersistenceService<Factura>  {
    final FacturaProveedorRepository repository;

    final ProductoProveedorService productoProveedorService;
    final SucursalService sucursalService;

    @Override
    public @NonNull FacturaProveedorRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Factura> clazz() {
        return Factura.class;
    }

    public List<Factura> readByDate(@NonNull LocalDate fecha) {
        var start = fecha.atStartOfDay();
        var end = fecha.atStartOfDay().plusDays(1).minusSeconds(1);
        return repository.findByFecha(start, end);
    }

    @Override
    @Transactional
    public Factura save(@NonNull Factura factura) throws LlaveDuplicadaException {
        Sucursal sucursal = sucursalService.readById(factura.getSucursal().getId());

        factura.getDetalle().stream()
                .peek(d -> {
                    ObjectId producto_id = d.getDetalle().getId();
                    d.setDetalle(productoProveedorService.readById(producto_id));
                })
                .forEach(sucursal.getBodega()::recepcionar);

        Factura saFactura = PersistenceService.super.save(factura);
        if (!sucursalService.update(sucursal))
            throw new MongoTransactionException("Error al actualizar la bodega de sucursal %s"
                    .formatted(sucursal.getNombre()));
        return saFactura;
    }

    @Override
    @Transactional
    public boolean delete(@NonNull ObjectId id) {
        Factura factura = readById(id);
        Sucursal sucursal = sucursalService.readById(factura.getSucursal());

        factura.getDetalle().stream()
                .peek(d -> {
                    ObjectId producto_id = d.getDetalle().getId();
                    d.setDetalle(productoProveedorService.readById(producto_id));
                })
                .forEach(sucursal.getBodega()::quitarExistencias);

        boolean res = PersistenceService.super.delete(id);

        if (res)
            if (!sucursalService.update(sucursal))
                throw new MongoTransactionException("Error al actualizar la bodega de sucursal %s"
                        .formatted(sucursal.getNombre()));
        return res;
    }
}
