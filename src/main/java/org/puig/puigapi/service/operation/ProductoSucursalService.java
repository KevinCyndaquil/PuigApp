package org.puig.puigapi.service.operation;

import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.admin.ProductoSucursalRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.admin.ProductoProveedorService;
import org.puig.puigapi.service.annotations.PuigService;
import org.springframework.beans.factory.annotation.Autowired;

@Setter(onMethod = @__({@Autowired}))
@PuigService(Sucursal.Producto.class)
public class ProductoSucursalService
        extends PersistenceService<Sucursal.Producto, String, ProductoSucursalRepository> {

    @NonNull protected ProductoProveedorService productoProveedorService;

    public ProductoSucursalService(ProductoSucursalRepository repository) {
        super(repository);
    }

    /**
     * Guarda un Producto de Sucursal Basandose en un Producto de Proveedor.
     * @param id del producto de proveedor.
     * @return el objeto encontrado o en su defecto, insertado.
     */

    public Sucursal.Producto saveOrReadById(@NotNull String id) {
        try {
            return findByProductoProveedor_id(id);
        } catch (BusquedaSinResultadoException e) {
            Proveedor.Producto productoProveedor = productoProveedorService.readByID(id);
            return save(new Sucursal.Producto(productoProveedor, true));
        }
    }

    /**
     * Devuelve un Producto de Sucursal según su Producto de Proveedor asignado.
     * @param id es la llave del producto proveedor a buscar.
     * @return el Producto de Sucursal Encontrado
     */
    public Sucursal.Producto findByProductoProveedor_id(String id) {
        return repository.findByProducto_proveedor(id)
                .orElseThrow(() -> new BusquedaSinResultadoException(
                        Sucursal.Producto.class,
                        "producto_proveedor.id",
                        id));
    }
}
