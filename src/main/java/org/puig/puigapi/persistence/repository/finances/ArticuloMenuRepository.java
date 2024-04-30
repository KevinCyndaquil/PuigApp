package org.puig.puigapi.persistence.repository.finances;

import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.puig.puigapi.util.Articulo;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloMenuRepository
        extends PuigRepository<ArticuloMenu, String> {

    @Query("{categoria: ?0}")
    List<ArticuloMenu> findByCategoria(Articulo.Categorias categoria);
}
