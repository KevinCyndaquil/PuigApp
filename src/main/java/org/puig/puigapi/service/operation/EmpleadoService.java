package org.puig.puigapi.service.operation;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repository.operation.EmpleadoRepository;
import org.puig.puigapi.service.auth.AuthService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.util.data.Tokenisable;
import org.puig.puigapi.util.persistence.Credentials;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Setter(onMethod = @__({@Autowired}))
@PuigService(Empleado.class)
public class EmpleadoService extends AuthService<Empleado, EmpleadoRepository> {

    protected SucursalService sucursalService;

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

    @Transactional
    public Tokenisable<Empleado> alta(@NotNull Empleado empleado, SimpleInstance<String> sucursalInstance) {
        Tokenisable<Empleado> token = register(empleado);
        sucursalService.generarAlta(sucursalInstance, empleado);
        return token;
    }

    @Transactional
    public Empleado actualizarEmpleado(@NotNull SimpleInstance<String> sucursalInstance,
                                       @NotNull SimpleInstance<ObjectId> empleadoInstance,
                                       @NotNull Empleado.Estados estado) {
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
