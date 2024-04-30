package org.puig.puigapi.service.finances;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.repository.finances.ArticuloMenuRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;

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

    @Override
    public ArticuloMenu readById(@NotNull String id) throws BusquedaSinResultadoException {
        Object[] params = new Object[] {
                new Document("_class", ArticuloMenu.class.getName()),
                new Document("_class", Combo.class.getName()),
        };

        return repository.findById(id, params)
                .orElseThrow(() -> new BusquedaSinResultadoException("_id", id));
    }
}
