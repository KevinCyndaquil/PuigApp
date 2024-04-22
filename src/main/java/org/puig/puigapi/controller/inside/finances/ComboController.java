package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.service.finances.ComboService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("combos")
public class ComboController extends PersistenceController<Combo, String, Combo.Request> {

    protected ComboController(ComboService service) {
        super(service);
    }
}
