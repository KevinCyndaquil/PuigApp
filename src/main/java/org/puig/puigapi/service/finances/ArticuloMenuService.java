package org.puig.puigapi.service.finances;

import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.repositories.finances.ArticuloMenuRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@PuigService(ArticuloMenu.class)
public class ArticuloMenuService extends
        PersistenceService<ArticuloMenu, String, ArticuloMenuRepository> {

    public ArticuloMenuService(ArticuloMenuRepository repository) {
        super(repository);
    }

    public List<ArticuloMenu> readByCategoria(ArticuloMenu.Categorias categoria) {
        return repository.findByCategoria(categoria);
    }
}
