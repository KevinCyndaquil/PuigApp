package org.puig.puigapi.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.exceptions.Errors;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends Response {
    protected Errors error;
    protected String hint;
}
