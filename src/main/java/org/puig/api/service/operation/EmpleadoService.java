package org.puig.api.service.operation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.configuration.jwt.JwtService;
import org.puig.api.persistence.repository.PuigRepository;
import org.puig.api.service.PersistenceService;
import org.puig.api.service.auth.AuthService;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.util.errors.LlaveDuplicadaException;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.persistence.repository.operation.EmpleadoRepository;
import org.puig.api.util.data.Tokenisable;
import org.puig.api.util.persistence.Credentials;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoService implements PersistenceService<Empleado>, AuthService<Empleado> {
    final EmpleadoRepository repository;
    final JwtService jwtService;

    final SucursalService sucursalService;

    @Override
    public JwtService jwtService() {
        return jwtService;
    }

    @Override
    public @NonNull PuigRepository<Empleado> repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Empleado> clazz() {
        return Empleado.class;
    }

    public Optional<Empleado> readByNickname(@NonNull String nickname) {
        return repository.findByNickname(nickname, Empleado.class.getName());
    }

    public boolean exists(@NonNull String nickname) {
        return readByNickname(nickname).isPresent();
    }

    @Override
    public Optional<Empleado> readByCredentials(@NonNull Credentials credentials) {
        return readByNickname(credentials.identifier());
    }

    @Override
    public Empleado save(@NonNull Empleado empleado) throws LlaveDuplicadaException {
        if (exists(empleado.getNickname()))
            throw new LlaveDuplicadaException(Empleado.class, empleado.getNickname());
        return AuthService.super.save(empleado);
    }

    @Transactional
    public Tokenisable<Empleado> alta(@NonNull Empleado empleado, SimpleInstance sucursalInstance) {
        Tokenisable<Empleado> token = register(empleado);
        sucursalService.generarAlta(sucursalInstance, empleado);
        return token;
    }

    @Transactional
    public Empleado actualizarEmpleado(@NonNull SimpleInstance sucursalInstance,
                                       @NonNull SimpleInstance empleadoInstance,
                                       @NonNull Empleado.Estados estado) {
        try {
            Sucursal reSucursal = sucursalService.readById(sucursalInstance);
            Empleado reEmpleado = readById(empleadoInstance);
            reSucursal.generar(reEmpleado, estado);

            if (!sucursalService.update(reSucursal))
                throw new MongoTransactionException("Error durante la asignación del estado %s al empleado %s a la sucursal %s"
                        .formatted(estado, reEmpleado.getNombre(), reSucursal.getNombre()));
            return reEmpleado;
        } catch (BusquedaSinResultadoException e) {
            throw new MongoTransactionException("Error durante la transacción. Motivo: " + e.getMessage());
        }
    }
}
