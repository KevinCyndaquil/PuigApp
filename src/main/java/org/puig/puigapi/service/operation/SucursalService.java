package org.puig.puigapi.service.operation;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.AltaEmpleadoInterrumpidaException;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.operation.SucursalRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.transaction.annotation.Transactional;

@Setter(onMethod = @__({@Autowired}))
@PuigService(Sucursal.class)
public class SucursalService extends
        PersistenceService<Sucursal, String, SucursalRepository> {

    protected EmpleadoService empleadoService;

    public SucursalService(SucursalRepository repository) {
        super(repository);
    }

    @Transactional
    public Empleado generarAlta(@NotNull Sucursal sucursal, @NotNull Empleado empleado) {
        try {
            Empleado saEmpleado = empleadoService.save(empleado);
            Sucursal saSucursal = readByID(sucursal);
            saSucursal.alta(saEmpleado);

            if (!update(saSucursal))
                throw new AltaEmpleadoInterrumpidaException(
                        AltaEmpleadoInterrumpidaException.Reasons.TRANSACTION_ERROR,
                        sucursal,
                        empleado);

            return saEmpleado;
        } catch (BusquedaSinResultadoException e) {
            throw new AltaEmpleadoInterrumpidaException(
                    AltaEmpleadoInterrumpidaException.Reasons.ID_ERROR,
                    sucursal,
                    empleado);
        }
    }

    @Transactional
    public Empleado actualizarEmpleado(@NotNull Sucursal sucursal,
                                       @NotNull Empleado empleado,
                                       @NotNull Empleado.Estados estado) {
        try {
            Sucursal reSucursal = readByID(sucursal);
            Empleado reEmpleado = empleadoService.readByID(empleado);
            reSucursal.generar(reEmpleado, estado);

            if (!update(reSucursal))
                throw new MongoTransactionException("Error durante la asignación del estado %s al empleado %s a la sucursal %s"
                        .formatted(estado, reEmpleado.getNombre(), reSucursal.getNombre()));
            return reEmpleado;
        } catch (BusquedaSinResultadoException e) {
            throw new MongoTransactionException("Error durante la transacción. Motivo: " + e.getMessage());
        }
    }
}
