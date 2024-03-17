package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.finances.Articulo;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloMenuRepository extends MongoRepository<ArticuloMenu, String> {
}
