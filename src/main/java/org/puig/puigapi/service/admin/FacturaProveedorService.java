package org.puig.puigapi.service.admin;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.errors.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.admin.FacturaProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.puig.puigapi.persistence.entity.admin.Proveedor.*;

@Service
public class FacturaProveedorService extends PersistenceService<Factura, String>  {
    protected ProductoProveedorService proProveedorService;
    protected ProductoSucursalService proTiendaService;
    protected SucursalService sucursalService;

    @Autowired
    public void setProProveedorService(ProductoProveedorService proProveedorService) {
        this.proProveedorService = proProveedorService;
    }

    @Autowired
    public void setProTiendaService(ProductoSucursalService proTiendaService) {
        this.proTiendaService = proTiendaService;
    }

    @Autowired
    public void setSucursalService(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @Autowired
    public FacturaProveedorService(FacturaProveedorRepository repository) {
        super(repository, Factura.class);
    }

    @Override
    @Transactional
    public Factura save(@NotNull Factura factura) throws LlaveDuplicadaException {
        Sucursal sucursal = sucursalService.readByID(factura.getSucursal().getId());

        factura.getDetalle().forEach(d -> {
            String proProId = d.getObjeto().getId();
            Proveedor.Producto proProveedor = proProveedorService.readByID(proProId);
            Sucursal.Producto proSucursal = proTiendaService.saveOrGet(proProveedor);

            d.setObjeto(proProveedor);
            sucursal.agregarExistencia(proSucursal, d.getCantidad());
        });
        factura.update();

        Factura saved = super.save(factura);
        if (!sucursalService.update(sucursal)) throw new RuntimeException("Doing rollback");

        return saved;
    }

    @Override
    public boolean delete(@NotNull String id) {
        Factura factura = readByID(id);
        Sucursal sucursal = sucursalService.readByID(factura.getSucursal().getId());

        factura.getDetalle().forEach(d -> {
            String proProId = d.getObjeto().getId();
            Sucursal.Producto proSucursal = proTiendaService.findByProductoProveedorId(proProId);

            boolean res = sucursal.quitarExistencias(proSucursal, d.getCantidad());
            System.out.println("delete bodega " + res);
        });

        boolean result = super.delete(id);
        System.out.println(sucursal);

        if (result)
            if (!sucursalService.update(sucursal))
                throw new RuntimeException("Doing rollback");
        return super.delete(id);
    }
}
