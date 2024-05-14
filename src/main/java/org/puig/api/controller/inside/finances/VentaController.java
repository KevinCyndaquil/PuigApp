package org.puig.api.controller.inside.finances;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.bson.types.ObjectId;
import org.puig.api.controller.PdfController;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.persistence.entity.finances.Venta;
import org.puig.api.persistence.entity.finances.Venta.ModosDeEntrega;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Empleado.Asignar;
import org.puig.api.service.PersistenceService;
import org.puig.api.service.finances.VentaService;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController implements PersistenceController<Venta>, PdfController {
    final VentaService service;
    final PuigLogger logger = new PuigLogger(VentaController.class);

    @Override
    public PersistenceService<Venta> service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB') or hasAuthority('GERENTE')")
    @GetMapping("where/date/is/in_range")
    public ResponseEntity<Response> readByFecha_venta(@RequestParam("from") LocalDate from,
                                                      @RequestParam("to") LocalDate to) {
        List<Venta> ventas = service.readByPeriodo(from, to);
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .body(ventas)
                .message("Ventas del periodo %s al %s obtenidas correctamente"
                        .formatted(from, to))
                .build()
                .transform();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB') or hasAuthority('GERENTE')")
    @GetMapping("where/date")
    public ResponseEntity<Response> readByFecha_venta(@RequestParam("from") LocalDate from) {
        List<Venta> ventas = service.readByPeriodo(from);
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .body(ventas)
                .message("Ventas del periodo %s obtenidas correctamente"
                        .formatted(from))
                .build()
                .transform();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB') or hasAuthority('GERENTE')")
    @GetMapping("reports/ventas_producto/where")
    public ResponseEntity<byte[]> generateProductosReport(
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to,
            @RequestParam("sucursal") ObjectId sucursalId) {

        try {
            JasperPrint print = service.generarReporteVentasProducto(
                    SimpleInstance.of(sucursalId),
                    from,
                    to);

            return ResponseEntity.ok()
                    .headers(generatePdfHeader("reporte-ventas"))
                    .body(service.exportPdf(print));
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB') or hasAuthority('GERENTE')")
    @GetMapping("reports/total_ventas/where")
    public ResponseEntity<Response> generateVentaReport(
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to,
            @RequestParam("filter") ModosDeEntrega filtro) {

        List<Venta> reporte =
                service.generarReporteVentas(from, to, filtro);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .body(reporte)
                .message("Ventas de %s encontradas con exito"
                        .formatted(filtro))
                .build()
                .transform();
    }

    @PostMapping("asignar")
    public ResponseEntity<Response> asignar(@NonNull@Valid@RequestBody Asignar asignar) {
        service.asignar(
                asignar.venta().instance(Venta.class),
                asignar.empleado().instance(Empleado.class));

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Venta asignada correctamente")
                .build()
                .transform();
    }
}
