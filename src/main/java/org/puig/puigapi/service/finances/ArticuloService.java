package org.puig.puigapi.service.finances;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.repository.finances.ArticuloRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.Articulo;
import org.puig.puigapi.util.annotation.PuigService;

@PuigService(Articulo.class)
public class ArticuloService extends PersistenceService<Articulo, String, ArticuloRepository> {

    protected ArticuloService(ArticuloRepository repository) {
        super(repository);
    }

    @Override
    public Articulo readById(@NotNull String id) throws BusquedaSinResultadoException {
        Object[] params = new Object[] {
                new Document("_class", ArticuloMenu.class.getName()),
                new Document("_class", Combo.class.getName()),
        };

        return repository.findById(id, params)
                .orElseThrow(() -> new BusquedaSinResultadoException("_id", id));
    }
}
