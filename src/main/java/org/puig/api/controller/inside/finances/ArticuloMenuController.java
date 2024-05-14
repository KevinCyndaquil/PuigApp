package org.puig.api.controller.inside.finances;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.persistence.entity.finances.ArticuloMenu;
import org.puig.api.service.finances.ArticuloMenuService;
import org.puig.api.util.PuigLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articulos_menu")
@RequiredArgsConstructor
public class ArticuloMenuController implements PersistenceController<ArticuloMenu> {
    final ArticuloMenuService service;
    final PuigLogger logger = new PuigLogger(ArticuloMenuController.class);

    @Override
    public ArticuloMenuService service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
    }

    @GetMapping(value = "where/categoria/is", produces = "application/json")
    public ResponseEntity<Response> readByCategoria(
            @NonNull@RequestParam("categoria") ArticuloMenu.Categorias categoria) {
        var result = service.readByCategoria(categoria);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Articulos Menu de %s encontrados"
                        .formatted(categoria))
                .body(result)
                .build()
                .transform();
    }
}
