package org.puig.api.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.controller.responses.ErrorResponse;
import org.puig.api.util.errors.Errors;
import org.puig.api.service.auth.PersonaAuthService;
import org.puig.api.util.PuigUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PersonaAuthService personaService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filter(request);
            filterChain.doFilter(request, response);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            write(response, ErrorResponse.builder()
                    .error(Errors.sin_autorizacion_error)
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Token provided is invalid")
                    .hint("Try to get another one or call to admin")
                    .build());
        } catch (ExpiredJwtException e) {
            write(response, ErrorResponse.builder()
                    .error(Errors.sesion_expirada_error)
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Sesion is expired")
                    .hint("Login again")
                    .build());
        } catch (UsernameNotFoundException e) {
            write(response, ErrorResponse.builder()
                    .error(Errors.nombreusuario_no_existente_error)
                    .status(HttpStatus.BAD_REQUEST)
                    .message("User provided is not registered or invalid")
                    .hint("Get registered now")
                    .build());
        }
    }

    private void filter(@NonNull HttpServletRequest request)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
            SignatureException, UsernameNotFoundException, IllegalArgumentException  {

        final String token = getToken(request);
        final String username;

        if (token == null) return;

        username = jwtService.claims(token).getSubject();

        if (username == null &&
                SecurityContextHolder.getContext().getAuthentication() != null) return;

        PuigUser user = personaService.readById(username);
        System.out.println("entering: " + username);
        System.out.println(user);
        if (!jwtService.validate(token, user)) return;

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String getToken(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void write(@NonNull HttpServletResponse response,
                       @NonNull ErrorResponse error) {
        try {
            response.setStatus(error.getStatus().value());
            response.setContentType("application/json");

            OutputStream outputStream = response.getOutputStream();
            mapper.writeValue(outputStream, error);
            outputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException("Could not write in response while the token given is not valid");
        }
    }
}
