package org.puig.api.service.operation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.puig.api.persistence.repository.PuigRepository;
import org.puig.api.util.errors.AltaEmpleadoInterrumpidaException;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.util.errors.LlaveDuplicadaException;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.persistence.repository.operation.SucursalRepository;
import org.puig.api.service.PersistenceService;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SucursalService implements PersistenceService<Sucursal> {
    final SucursalRepository repository;

    @Override
    public @NonNull PuigRepository<Sucursal> repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Sucursal> clazz() {
        return Sucursal.class;
    }

    public Set<Empleado.Detalle> obtenerEmpleados(@NonNull ObjectId sucursal_id) {
        return readById(sucursal_id).getEmpleados();
    }

    public Sucursal.Bodega obtenerBodega(@NonNull ObjectId sucursal_id) {
        return readById(sucursal_id).getBodega();
    }

    @Transactional
    public Empleado generarAlta(@NonNull SimpleInstance sucursalInstance, @NonNull Empleado empleado) {
        try {
            Sucursal saSucursal = readById(sucursalInstance);
            saSucursal.alta(empleado);

            if (!update(saSucursal))
                throw new AltaEmpleadoInterrumpidaException(
                        AltaEmpleadoInterrumpidaException.Reasons.TRANSACTION_ERROR,
                        sucursalInstance.instance(Sucursal.class),
                        empleado);

            return empleado;
        } catch (BusquedaSinResultadoException e) {
            throw new AltaEmpleadoInterrumpidaException(
                    AltaEmpleadoInterrumpidaException.Reasons.ID_ERROR,
                    sucursalInstance.instance(Sucursal.class),
                    empleado);
        }
    }

    @Override
    public Sucursal save(@NonNull Sucursal sucursal) throws LlaveDuplicadaException {
        DecimalFormat decimalFormat = new DecimalFormat("00000");
        int quantity = readAllWhile().size();
        sucursal.setCodigo(decimalFormat.format(quantity));

        return PersistenceService.super.save(sucursal);
    }
}
