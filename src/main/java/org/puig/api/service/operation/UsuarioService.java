package org.puig.api.service.operation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.configuration.jwt.JwtService;
import org.puig.api.service.PersistenceService;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.util.errors.LlaveDuplicadaException;
import org.puig.api.persistence.entity.operation.Usuario;
import org.puig.api.util.data.Correo;
import org.puig.api.util.data.Telefono;
import org.puig.api.persistence.repository.operation.UsuarioRepository;
import org.puig.api.service.auth.AuthService;
import org.puig.api.util.persistence.Credentials;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements PersistenceService<Usuario>, AuthService<Usuario> {
    final UsuarioRepository repository;
    final JwtService jwtService;

    @Override
    public @NonNull UsuarioRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Usuario> clazz() {
        return Usuario.class;
    }

    @Override
    public JwtService jwtService() {
        return jwtService;
    }

    @Override
    public Optional<Usuario> readByCredentials(@NonNull Credentials credentials) {
        return readByCorreoOrTelefono(credentials.identifier());
    }

    @Override
    public Usuario save(@NonNull Usuario usuario) throws LlaveDuplicadaException {
        if (exists(usuario.getCorreo(), usuario.getTelefono()))
            throw new LlaveDuplicadaException(Usuario.class, usuario.getCorreo(), usuario.getTelefono());
        return AuthService.super.save(usuario);
    }

    public Usuario saveOrGetAsCliente(@NonNull Usuario usuario) throws LlaveDuplicadaException {
        try {
            return readByTelefono(usuario.getTelefono());
        } catch (BusquedaSinResultadoException e) {
            return repository.insert(usuario);
        }
    }

    public Usuario readByCorreo(@NonNull Correo correo) throws BusquedaSinResultadoException {
        return repository.findByCorreo(
                correo.getDireccion(),
                Usuario.class.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException(Usuario.class, "correo", correo));
    }

    public Usuario readByTelefono(@NonNull Telefono telefono) throws BusquedaSinResultadoException {
        return repository.findByTelefono(
                telefono.getNumero(),
                Usuario.class.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException(Usuario.class, "telefono", telefono.getNumero()));
    }

    public Optional<Usuario> readByCorreoOrTelefono(@NonNull String identifier) {
        return repository.findByCorreoOrTelefono(identifier, Usuario.class.getName());
    }

    public boolean exists(@NonNull Correo correo,
                          @NonNull Telefono telefono) {
        return repository.findByCorreoOrTelefono(
                correo.getDireccion(),
                telefono.getNumero(),
                Usuario.class.getName()).isPresent();
    }
}
