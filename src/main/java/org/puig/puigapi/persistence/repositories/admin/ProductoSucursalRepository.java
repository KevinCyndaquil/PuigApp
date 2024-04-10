package org.puig.puigapi.persistence.repositories.admin;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoSucursalRepository
        extends PuigRepository<Sucursal.Producto, String> {

    @Query("{'producto_proveedor.id': ?0}")
    Optional<Sucursal.Producto> findByProducto_proveedor(String id);
}
