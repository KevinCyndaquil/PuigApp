package org.puig.puigapi.persistence.repositories.operation;

import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository
        extends PuigRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String _correo);
}
