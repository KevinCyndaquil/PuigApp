package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.service.finances.ComboService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("combos")
public class ComboController extends PersistenceController<Combo, String, Combo.PostRequest> {

    protected ComboController(ComboService service) {
        super(service);
    }

    @Override
    @PreAuthorize("hasAuthority('GERENTE') or hasAnyAuthority('ADMINISTRADOR_WEB')")
    public ResponseEntity<Response> readAll() {
        return super.readAll();
    }
}
