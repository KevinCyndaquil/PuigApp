package org.puig.puigapi.controller.responses;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.jackson.PuigMapper;
import org.springframework.http.ResponseEntity;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectResponse extends Response {
    protected @JsonRawValue Object body;

    @Override
    public ResponseEntity<Response> transform() {
        try {
            ObjectMapper mapper = new PuigMapper();
            body = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return super.transform();
    }
}
