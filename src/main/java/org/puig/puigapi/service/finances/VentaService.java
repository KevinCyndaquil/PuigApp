package org.puig.puigapi.service.finances;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.errors.CreacionVentaException;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repositories.finances.VentaRepartoRepository;
import org.puig.puigapi.persistence.repositories.finances.VentaRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService extends PersistenceService<Venta, String> {
    protected ArticuloMenuService articuloMenuService;
    protected EmpleadoService empleadoService;

    @Autowired public void setArticuloMenuService(ArticuloMenuService articuloMenuService) {
        this.articuloMenuService = articuloMenuService;
    }

    @Autowired public void setEmpleadoService(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @Autowired
    public VentaService(VentaRepository repository) {
        super(repository, Venta.class);
    }

    @Override
    public Venta save(@NotNull Venta venta) {
        asignarPrecioDetalle(venta);
        venta.setMonto_total(venta.getMonto_total());
        venta.setPago_total(venta.getPago_total());

        //Obtener el puesto del empleado
        Empleado asignada_a = empleadoService.readByID(venta.getAsignada_a().getId());
        venta.getAsignada_a().setPuesto(asignada_a.getPuesto());

        if (!venta.esValida())
            throw CreacionVentaException.pagoInferiorAMonto(venta);

        return super.save(venta);
    }

    protected void asignarPrecioDetalle(@NotNull Venta venta) {
        venta.getDetalle().forEach(d -> {
            String id = d.getObjeto().getId();
            ArticuloMenu articuloMenu = articuloMenuService.readPrecioById(id);
            d.setObjeto(articuloMenu); //save precio and monto
        });
    }

    @Service
    public static class Reparto extends PersistenceService<Venta.Reparto, String> {

        @Autowired
        public Reparto(VentaRepartoRepository repository) {
            super(repository, Venta.Reparto.class);
        }

        @Override
        public List<Venta.Reparto> readAll() {
            return repository.findAllByClass(Venta.Reparto.class.getCanonicalName());
        }
    }
}
