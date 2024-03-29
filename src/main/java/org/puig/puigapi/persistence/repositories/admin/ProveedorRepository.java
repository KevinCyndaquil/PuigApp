package org.puig.puigapi.persistence.repositories.admin;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends MongoRepository<Proveedor, String> {

}
