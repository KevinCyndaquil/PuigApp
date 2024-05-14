package org.puig.api.persistence.repository.finances;

import org.puig.api.persistence.entity.finances.ArticuloMenu;
import org.puig.api.persistence.repository.PuigRepository;
import org.puig.api.util.Articulo;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloMenuRepository extends PuigRepository<ArticuloMenu> {

    @Query("{categoria: ?0}")
    List<ArticuloMenu> findByCategoria(Articulo.Categorias categoria);
}
