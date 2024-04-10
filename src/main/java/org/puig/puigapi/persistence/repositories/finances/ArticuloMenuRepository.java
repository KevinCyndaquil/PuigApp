package org.puig.puigapi.persistence.repositories.finances;

import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloMenuRepository
        extends PuigRepository<ArticuloMenu, String> {
    @Query(value = "{'_id': ?0}", fields = "{'precio': 1, '_nombre': 1}")
    ArticuloMenu findPrecioById(String id);
}
