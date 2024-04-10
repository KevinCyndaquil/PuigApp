package org.puig.puigapi.service.admin;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.errors.BusquedaSinResultadoException;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.admin.ProductoSucursalRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductoSucursalService
        extends PersistenceService<Sucursal.Producto, String> {

    protected ProductoSucursalRepository repository;
    protected MongoTemplate template;

    @Autowired
    public void setTemplate(MongoTemplate template) {
        this.template = template;
    }

    @Autowired
    public ProductoSucursalService(ProductoSucursalRepository repository) {
        super(repository, Sucursal.Producto.class);
        this.repository = repository;
    }

    /**
     * Guarda un Producto de Sucursal Basandose en un Producto de Proveedor.
     * @param productoProveedor al que se basará para su instancia.
     * @return el objeto encontrado o en su defecto, insertado.
     */
    public Sucursal.Producto saveOrGet(@NotNull Proveedor.Producto productoProveedor) {
        try {
            return findByProductoProveedorId(productoProveedor.getId());
        } catch (BusquedaSinResultadoException e) {
            return save(new Sucursal.Producto(productoProveedor, true));
        }
    }


    /**
     * Devuelve un Producto de Sucursal según su Producto de Proveedor asignado.
     * @param id es la llave del producto proveedor a buscar.
     * @return el Producto de Sucursal Encontrado
     */
    public Sucursal.Producto findByProductoProveedorId(String id) {
        return repository.findByProducto_proveedor(id)
                .orElseThrow(() -> new BusquedaSinResultadoException(
                        Sucursal.Producto.class,
                        "producto_proveedor.id",
                        id));
    }
}
