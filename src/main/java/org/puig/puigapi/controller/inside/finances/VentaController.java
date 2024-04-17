package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.service.finances.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController extends PersistenceController<Venta, String, Venta.Request> {
    protected VentaService service;

    public VentaController(VentaService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("where/date/in_range")
    public ResponseEntity<Response> readByFecha_venta(@RequestParam("from") LocalDate from,
                                                     @RequestParam("to") LocalDate to) {
        List<Venta> ventas = service.readByFecha_venta(from, to);
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .body(ventas)
                .message("Las ventas durante esas fechas fueron encontradas")
                .build()
                .transform();
    }

    @GetMapping("where/date/is")
    public ResponseEntity<Response> readByFecha_venta(@RequestParam("from") LocalDate from) {
        List<Venta> ventas = service.readByFecha_venta(from);
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .body(ventas)
                .message("Las ventas durante esa fecha fueron encontradas")
                .build()
                .transform();
    }
}
