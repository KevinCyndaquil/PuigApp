package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.service.finances.ArticuloMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articulos_menu")
public class ArticuloMenuController
        extends PersistenceController<ArticuloMenu, String, ArticuloMenu.PostRequest> {
    protected ArticuloMenuService service;

    protected ArticuloMenuController(ArticuloMenuService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("where/categoria/is")
    public ResponseEntity<Response> readByCategoria(
            @RequestParam("categoria") ArticuloMenu.Categorias categoria) {
        var result = service.readByCategoria(categoria);
        return ObjectResponse.builder()
                .status(HttpStatus.FOUND)
                .message("Articulos Menu de %s encontrados"
                        .formatted(categoria))
                .body(result)
                .build()
                .transform();
    }
}
