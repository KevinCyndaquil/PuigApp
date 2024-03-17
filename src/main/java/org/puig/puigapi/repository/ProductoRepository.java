package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.admin.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
}
