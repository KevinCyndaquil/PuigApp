package org.puig.puigapi.service.operation;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.util.data.Correo;
import org.puig.puigapi.util.data.Telefono;
import org.puig.puigapi.persistence.repository.operation.UsuarioRepository;
import org.puig.puigapi.service.auth.AuthService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.util.persistence.Credentials;

import java.util.Optional;

@PuigService(Usuario.class)
public class UsuarioService extends AuthService<Usuario, UsuarioRepository> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    public Usuario saveOrReadById(@NotNull Usuario usuario)
            throws LlaveDuplicadaException {
        try {
            return readByTelefono(usuario.getTelefono());
        } catch (BusquedaSinResultadoException e) {
            return repository.insert(usuario);
        }
    }

    public Usuario readByCorreo(@NotNull @Valid Correo correo)
            throws BusquedaSinResultadoException {
        return repository.findByCorreo(
                correo.getDireccion(),
                Usuario.class.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException(Usuario.class, "correo", correo));
    }

    @Valid
    public Usuario readByTelefono(@NotNull @Valid Telefono telefono)
            throws BusquedaSinResultadoException {
        return repository.findByTelefono(
                telefono.getNumero(),
                Usuario.class.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException(Usuario.class, "telefono", telefono.getNumero()));
    }

    public Optional<Usuario> readByCorreoOrTelefono(@NotNull String identifier) {
        return repository.findByCorreoOrTelefono(identifier, Usuario.class.getName());
    }

    public boolean exists(@NotNull @Valid Correo correo,
                          @NotNull @Valid Telefono telefono) {
        return repository.findByCorreoOrTelefono(
                correo.getDireccion(),
                telefono.getNumero(),
                Usuario.class.getName()).isPresent();
    }

    @Override
    public Optional<Usuario> readByCredentials(@NotNull Credentials<String> credentials) {
        return readByCorreoOrTelefono(credentials.identifier());
    }

    @Valid
    @Override
    public Usuario save(@NotNull Usuario usuario) throws LlaveDuplicadaException {
        if (exists(usuario.getCorreo(), usuario.getTelefono()))
            throw new LlaveDuplicadaException(Usuario.class, usuario.getCorreo(), usuario.getTelefono());
        return super.save(usuario);
    }
}
