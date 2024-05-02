package org.puig.puigapi.service.operation;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repository.operation.EmpleadoRepository;
import org.puig.puigapi.service.auth.AuthService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.util.persistence.Credentials;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Setter(onMethod = @__({@Autowired}))
@PuigService(Empleado.class)
public class EmpleadoService extends AuthService<Empleado, EmpleadoRepository> {

    public EmpleadoService(EmpleadoRepository repository) {
        super(repository);
    }

    public Optional<Empleado> readByNickname(@NotNull String nickname) {
        return repository.findByNickname(nickname, Empleado.class.getName());
    }

    public boolean exists(@NotNull String nickname) {
        return readByNickname(nickname).isPresent();
    }

    @Override
    public Optional<Empleado> readByCredentials(@NotNull Credentials credentials) {
        return readByNickname(credentials.identifier());
    }

    @Override
    public Empleado save(@NotNull Empleado empleado) throws LlaveDuplicadaException {
        if (exists(empleado.getNickname()))
            throw new LlaveDuplicadaException(Empleado.class, empleado.getNickname());
        return super.save(empleado);
    }
}
