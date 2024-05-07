package org.puig.puigapi.controller.inside.admin;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.ProductoProveedorService;
import org.puig.puigapi.service.admin.ProveedorService;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Setter(onMethod_ = @Autowired)
@RestController
@RequestMapping("/proveedores/productos")
public class ProductoProveedorController
        extends PersistenceController<Proveedor.Producto, ObjectId, Proveedor.Producto.PostRequest> {
    protected ProductoProveedorService service;
    protected ProveedorService proveedorService;

    @Autowired
    protected ProductoProveedorController(ProductoProveedorService service) {
        super(service);
        this.service = service;
    }

    @PostMapping(value = "where/proveedor/id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readProductosByProveedorId(@NotNull @RequestBody SimpleInstance<ObjectId> proveedorInstance) {

        System.out.println(proveedorInstance.id());
        Proveedor proveedor = proveedorService.readById(proveedorInstance);
        System.out.println(proveedor);
        Set<Proveedor.Producto> productos = service.readByProveedor(proveedorInstance);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Productos del proveedor %s encontrados correctamente"
                        .formatted(proveedor.getNombre()))
                .body(productos)
                .build()
                .transform();
    }
}
