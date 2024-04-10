package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.service.finances.ArticuloMenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articulos_menu")
public class ArticuloMenuController
        extends PersistenceController<ArticuloMenu, String, ArticuloMenu.Request> {
    protected ArticuloMenuService service;

    protected ArticuloMenuController(ArticuloMenuService service) {
        super(service);
        this.service = service;
    }
}
