package org.puig.puigapi.persistence.repositories.admin;

import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository
        extends PuigRepository<ProductoTienda, String> {
}
