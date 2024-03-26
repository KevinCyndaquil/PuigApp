package org.puig.puigapi.controller;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Credentials;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController <U extends Persona, ID> {
    @PostMapping("auth/register")
    @NotNull
    ResponseEntity<String> register(@RequestBody U u);

    @PostMapping("auth/login")
    @NotNull
    ResponseEntity<String> login(@RequestBody Credentials<ID> credential);
}
