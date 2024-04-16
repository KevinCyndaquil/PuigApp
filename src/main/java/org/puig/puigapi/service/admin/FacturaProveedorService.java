package org.puig.puigapi.service.admin;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.admin.FacturaProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.transaction.annotation.Transactional;

import static org.puig.puigapi.persistence.entity.admin.Proveedor.*;

@PuigService(Factura.class)
@Setter(onMethod_ = @Autowired)
public class FacturaProveedorService extends
        PersistenceService<Factura, String, FacturaProveedorRepository>  {

    protected ProductoProveedorService productoProveedorService;
    protected ProductoSucursalService productoSucursalService;

    protected SucursalService sucursalService;

    public FacturaProveedorService(FacturaProveedorRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Factura save(@NotNull Factura factura) throws LlaveDuplicadaException {
        Sucursal sucursal = sucursalService.readByID(factura.getSucursal().getId());

        factura.getDetalle().forEach(d -> {
            String productoProveedor_id = d.getObjeto().getId();
            Proveedor.Producto productoProveedor = productoProveedorService.readByID(productoProveedor_id);
            Sucursal.Producto productoSucursal = productoSucursalService.saveOrReadById(productoProveedor_id);

            d.setObjeto(productoProveedor);
            sucursal.agregarExistencia(productoSucursal, d.getCantidad());
        });
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
        Factura factura = readByID(id);
        Sucursal sucursal = sucursalService.readByID(factura.getSucursal());

        factura.getDetalle().forEach(d -> {
            String productoProveedor_id = d.getObjeto().getId();
            Sucursal.Producto proSucursal =
                    productoSucursalService.findByProductoProveedor_id(productoProveedor_id);

            sucursal.quitarExistencias(proSucursal, d.getCantidad());
        });

        boolean res = super.delete(id);

        if (res)
            if (!sucursalService.update(sucursal))
                throw new MongoTransactionException("Error al actualizar la bodega de sucursal %s");
        return res;
    }
}
