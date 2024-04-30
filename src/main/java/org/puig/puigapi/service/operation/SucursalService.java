package org.puig.puigapi.service.operation;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.AltaEmpleadoInterrumpidaException;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repository.operation.SucursalRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Set;

@Setter(onMethod = @__({@Autowired}))
@PuigService(Sucursal.class)
public class SucursalService extends
        PersistenceService<Sucursal, String, SucursalRepository> {

    protected EmpleadoService empleadoService;

    public SucursalService(SucursalRepository repository) {
        super(repository);
    }

    public Set<Empleado.Detalle> obtenerEmpleados(@NotNull String sucursal_id) {
        return readById(sucursal_id).getEmpleados();
    }

    public Sucursal.Bodega obtenerBodega(@NotNull String sucursal_id) {
        return readById(sucursal_id).getBodega();
    }

    @Transactional
    public Empleado generarAlta(@NotNull SimpleInstance<String> sucursalInstance, @NotNull Empleado empleado) {
        try {
            Empleado saEmpleado = empleadoService.save(empleado);
            Sucursal saSucursal = readById(sucursalInstance);
            saSucursal.alta(saEmpleado);

            if (!update(saSucursal))
                throw new AltaEmpleadoInterrumpidaException(
                        AltaEmpleadoInterrumpidaException.Reasons.TRANSACTION_ERROR,
                        sucursalInstance.instance(Sucursal.class),
                        empleado);

            return saEmpleado;
        } catch (BusquedaSinResultadoException e) {
            throw new AltaEmpleadoInterrumpidaException(
                    AltaEmpleadoInterrumpidaException.Reasons.ID_ERROR,
                    sucursalInstance.instance(Sucursal.class),
                    empleado);
        }
    }

    @Transactional
    public Empleado actualizarEmpleado(@NotNull SimpleInstance<String> sucursalInstance,
                                       @NotNull SimpleInstance<ObjectId> empleadoInstance,
                                       @NotNull Empleado.Estados estado) {
        try {
            Sucursal reSucursal = readById(sucursalInstance);
            Empleado reEmpleado = empleadoService.readById(empleadoInstance);
            reSucursal.generar(reEmpleado, estado);

            if (!update(reSucursal))
                throw new MongoTransactionException("Error durante la asignación del estado %s al empleado %s a la sucursal %s"
                        .formatted(estado, reEmpleado.getNombre(), reSucursal.getNombre()));
            return reEmpleado;
        } catch (BusquedaSinResultadoException e) {
            throw new MongoTransactionException("Error durante la transacción. Motivo: " + e.getMessage());
        }
    }

    @Override
    public Sucursal save(@NotNull Sucursal sucursal) throws LlaveDuplicadaException {
        DecimalFormat decimalFormat = new DecimalFormat("00000");
        int quantity = readAllWhile().size();
        sucursal.setId(decimalFormat.format(quantity));

        return super.save(sucursal);
    }
}
