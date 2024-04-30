package org.puig.puigapi.service.auth;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.puig.puigapi.service.operation.UsuarioService;
import org.puig.puigapi.util.Persona;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonaAuthService implements UserDetailsService {
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;

    public Persona readById(String identifier) throws UsernameNotFoundException {
        ObjectId id = new ObjectId(identifier);

        try {
            return empleadoService.readById(id);
        } catch (BusquedaSinResultadoException e1) {
            try {
                return usuarioService.readById(id);
            } catch (BusquedaSinResultadoException e2) {
                throw new UsernameNotFoundException(
                        "Id %s no encontrada ni como Usuario ni Empleado"
                                .formatted(id));
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return readById(username);
    }
}
