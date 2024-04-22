package org.puig.puigapi.service.operation;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.persistence.entity.utils.data.Correo;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.repositories.operation.UsuarioRepository;
import org.puig.puigapi.service.AuthService;
import org.puig.puigapi.service.annotations.PuigService;

@PuigService(Usuario.class)
public class UsuarioService extends AuthService<Usuario, UsuarioRepository> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    @Valid
    @Override
    public Usuario save(@NotNull Usuario usuario) throws LlaveDuplicadaException {
        if (exists(usuario.getCorreo(), usuario.getTelefono()))
            throw new LlaveDuplicadaException(Usuario.class, usuario.getCorreo(), usuario.getTelefono());
        return super.save(usuario);
    }

    public Usuario saveOrReadById(@NotNull Usuario usuario) throws LlaveDuplicadaException {
        try {
            return readByTelefono(usuario.getTelefono());
        } catch (BusquedaSinResultadoException e) {
            return repository.insert(usuario);
        }
    }

    @Valid
    public Usuario readByCorreo(@NotNull Correo correo)
            throws BusquedaSinResultadoException {
        return repository.findByCorreo(
                correo.getDireccion(),
                Usuario.class.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException(Usuario.class, "correo", correo));
    }

    @Valid
    public Usuario readByTelefono(@NotNull Telefono telefono)
            throws BusquedaSinResultadoException {
        return repository.findByTelefono(
                telefono.getNumero(),
                Usuario.class.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException(Usuario.class, "telefono", telefono.getNumero()));
    }

    @Valid
    public boolean exists(@NotNull Correo correo, @NotNull Telefono telefono) {
        return repository.findByCorreoOrTelefono(
                correo.getDireccion(),
                telefono.getNumero(),
                Usuario.class.getName()).isPresent();
    }
}
