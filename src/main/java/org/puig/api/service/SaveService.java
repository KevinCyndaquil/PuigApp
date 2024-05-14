package org.puig.api.service;

import lombok.NonNull;
import org.puig.api.persistence.repository.PuigRepository;
import org.puig.api.util.errors.LlaveDuplicadaException;
import org.puig.api.util.errors.NombreUnicoRepetidoException;
import org.puig.api.util.persistence.Nombrable;
import org.puig.api.util.persistence.Unico;

import java.util.Set;
import java.util.stream.Collectors;

public interface SaveService <C extends Unico> {
    @NonNull PuigRepository<C> repository();
    @NonNull Class<C> clazz();

    default C save(@NonNull C c) throws LlaveDuplicadaException, NombreUnicoRepetidoException {
        if (c.getId() != null)
            if (repository().existsById(c.getId()))
                throw new LlaveDuplicadaException(clazz(), c.getId());
        if (c instanceof Nombrable nombrable)
            if (repository().findByNombre(nombrable.getNombre(), clazz().getName()).isPresent())
                throw new NombreUnicoRepetidoException(c.getClass(), nombrable.getNombre());

        return repository().insert(c);
    }

    default Set<C> save(@NonNull Set<C> cs) throws LlaveDuplicadaException, NombreUnicoRepetidoException {
        return cs.stream().map(this::save).collect(Collectors.toSet());
    }
}
