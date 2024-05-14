package org.puig.api.controller.inside.finances;

import lombok.RequiredArgsConstructor;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.Response;
import org.puig.api.persistence.entity.finances.Combo;
import org.puig.api.service.finances.ComboService;
import org.puig.api.util.PuigLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("combos")
@RequiredArgsConstructor
public class ComboController implements PersistenceController<Combo> {
    final ComboService service;
    final PuigLogger logger = new PuigLogger(ComboController.class);

    @Override
    public ComboService service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
    }

    @Override
    @PreAuthorize("hasAuthority('GERENTE') or hasAnyAuthority('ADMINISTRADOR_WEB')")
    public ResponseEntity<Response> readAll() {
        return PersistenceController.super.readAll();
    }
}
