package org.puig.puigapi.controller.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UsuarioController extends PersistenceController<Usuario, String> {
    @Autowired
    public UsuarioController(UsuarioService service){super(service );}

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        logger.info("Petición de inicio de sesión a las %s".formatted(LocalDateTime.now()));

        if (service.ifExists(usuario)) {
            return new ResponseEntity<>("Inicio de sesión exitoso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
    }


}
