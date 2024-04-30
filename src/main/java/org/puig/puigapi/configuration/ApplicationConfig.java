package org.puig.puigapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.repository.operation.EmpleadoRepository;
import org.puig.puigapi.persistence.repository.operation.UsuarioRepository;
import org.puig.puigapi.service.auth.PersonaAuthService;
import org.puig.puigapi.util.Persona;
import org.puig.puigapi.util.jackson.PuigMapper;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final PersonaAuthService authService;

    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authService);
        return authenticationProvider;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new PuigMapper();
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(PersistenceController.class);
    }
}
