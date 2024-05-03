package org.puig.puigapi.util.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.puigapi.util.Persona;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tokenisable <U extends Persona> {
    private U user;
    private String token;
    private long expired_in;
}
