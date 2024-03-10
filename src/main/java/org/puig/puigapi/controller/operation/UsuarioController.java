package org.puig.puigapi.controller.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsuarioController extends PersistenceController<Usuario, String> {
    @Autowired
    public UsuarioController(UsuarioService service){super(service );}
}
