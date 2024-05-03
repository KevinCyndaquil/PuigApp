package org.puig.puigapi.controller.inside.finances;

import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.PdfController;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.finances.Venta.Reparto;
import org.puig.puigapi.service.finances.VentaService;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController extends PersistenceController<Venta, String, Venta.PostRequest> implements PdfController {
    protected VentaService service;

    public VentaController(VentaService service) {
        super(service);
        this.service = service;
    }

    @Valid
    @PostMapping("reparto")
    public ResponseEntity<Response> save(@NotNull @RequestBody Reparto.PostRequest postReparto) {
        Reparto reparto = service.save(postReparto.instance());

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Venta de reparto realizada exitosamente")
                .body(reparto)
                .build()
                .transform();
    }

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

    @GetMapping("where/date/is")
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

    @GetMapping("reports/ventas_producto/where")
    public ResponseEntity<byte[]> generateProductosReport(@RequestParam("from")LocalDate from,
                                                          @RequestParam("to") LocalDate to,
                                                          @RequestParam("sucursal") String sucursalId) {
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

    @GetMapping("reports/ventas/where/date/in_range")
    public ResponseEntity<Response> generateVentaReport(@RequestParam("from")LocalDate from,
                                                        @RequestParam("to") LocalDate to,
                                                        @RequestParam("filter") Venta.ModosDeEntrega filtro) {
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
}
