package org.puig.api.persistence.repository.admin;

import org.bson.types.ObjectId;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductoProveedorRepository extends PuigRepository<Proveedor.Producto> {

    @Query("{'proveedor.$id': ?0, '_class': 'org.puig.puigapi.persistence.entity.admin.Proveedor$Producto'}")
    Set<Proveedor.Producto> findByProveedorId(ObjectId proveedor_id);
}