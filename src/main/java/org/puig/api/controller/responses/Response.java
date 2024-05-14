package org.puig.api.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    protected HttpStatus status;
    protected String message;

    public ResponseEntity<Response> transform() {
        return ResponseEntity.status(status)
                .body(this);
    }
}
