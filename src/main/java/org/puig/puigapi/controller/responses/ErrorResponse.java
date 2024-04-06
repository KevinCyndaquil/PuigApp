package org.puig.puigapi.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.controller.responses.Response;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends Response {
    protected String error;
    protected String hint;
}
