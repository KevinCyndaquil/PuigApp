package org.puig.puigapi.configuration;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.repositories.operation.EmpleadoRepository;
import org.puig.puigapi.persistence.repositories.operation.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;

    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return id -> usuarioRepository.findByCorreo(id)
                .map(Persona.class::cast)
                .orElse(empleadoRepository.findByNickname(id)
                        .map(Persona.class::cast)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User or employee didn't find")));
    }
}
