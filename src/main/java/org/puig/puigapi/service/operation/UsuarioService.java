package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.persistence.repositories.operation.UsuarioRepository;
import org.puig.puigapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService extends AuthService<Usuario> {
    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        super(repository, Usuario.class);
    }

    @Override
    public List<Usuario> readAll() {
        return repository.findAllByClass(Usuario.class.getCanonicalName());
    }
}

