package org.puig.api.service.finances;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.puig.api.service.ReadService;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.persistence.entity.finances.ArticuloMenu;
import org.puig.api.persistence.entity.finances.Combo;
import org.puig.api.persistence.repository.finances.ArticuloRepository;
import org.puig.api.util.Articulo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticuloService implements ReadService<Articulo> {
    final ArticuloRepository repository;

    @Override
    public @NonNull ArticuloRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Articulo> clazz() {
        return Articulo.class;
    }

    @Override
    public Articulo readById(@NonNull ObjectId id) throws BusquedaSinResultadoException {
        Object[] params = new Object[] {
                new Document("_class", ArticuloMenu.class.getName()),
                new Document("_class", Combo.class.getName()),
        };

        return repository.findById(id, params)
                .orElseThrow(() -> new BusquedaSinResultadoException("_id", id));
    }
}
