package org.puig.puigapi.persistence.repository.admin;

import org.bson.types.ObjectId;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductoProveedorRepository
        extends PuigRepository<Proveedor.Producto, ObjectId> {

    @Query("{'proveedor.$id': ?0}")
    Set<Proveedor.Producto> findByProveedorId(ObjectId proveedor_id);
}
