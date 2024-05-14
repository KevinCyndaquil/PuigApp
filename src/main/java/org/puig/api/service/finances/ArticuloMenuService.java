package org.puig.api.service.finances;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.persistence.entity.finances.ArticuloMenu;
import org.puig.api.persistence.entity.finances.Combo;
import org.puig.api.persistence.repository.finances.ArticuloMenuRepository;
import org.puig.api.service.PersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloMenuService implements PersistenceService<ArticuloMenu> {
    final ArticuloMenuRepository repository;

    @Override
    public @NonNull ArticuloMenuRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<ArticuloMenu> clazz() {
        return ArticuloMenu.class;
    }

    public List<ArticuloMenu> readByCategoria(ArticuloMenu.Categorias categoria) {
        return repository.findByCategoria(categoria);
    }

    @Override
    public ArticuloMenu readById(@NonNull ObjectId id) throws BusquedaSinResultadoException {
        Object[] params = new Object[] {
                new Document("_class", ArticuloMenu.class.getName()),
                new Document("_class", Combo.class.getName()),
        };

        return repository.findById(id, params)
                .orElseThrow(() -> new BusquedaSinResultadoException("_id", id));
    }
}
