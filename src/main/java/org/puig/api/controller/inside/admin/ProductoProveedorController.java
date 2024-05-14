package org.puig.api.controller.inside.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.service.admin.ProductoProveedorService;
import org.puig.api.service.admin.ProveedorService;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/proveedores/productos")
@RequiredArgsConstructor
public class ProductoProveedorController implements PersistenceController<Proveedor.Producto> {
    final ProductoProveedorService service;
    final ProveedorService proveedorService;
    final PuigLogger logger = new PuigLogger(ProductoProveedorController.class);

    @Override
    public ProductoProveedorService service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
    }

    @PostMapping(value = "where/proveedor/id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readProductosByProveedorId(
            @NonNull@Validated(SimpleInfo.class)@RequestBody SimpleInstance proveedorInstance) {

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
