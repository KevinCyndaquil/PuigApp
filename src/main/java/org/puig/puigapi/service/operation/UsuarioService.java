package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.persistence.repositories.operation.UsuarioRepository;
import org.puig.puigapi.service.AuthService;
import org.puig.puigapi.service.annotations.PuigService;

@PuigService(Usuario.class)
public class UsuarioService extends AuthService<Usuario, UsuarioRepository> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }
}
