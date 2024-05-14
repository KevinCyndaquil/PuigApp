package org.puig.api.util.data;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.api.util.PuigUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tokenisable <U extends PuigUser> {
    private @Valid U user;
    private String token;
    private long expired_in;
}
