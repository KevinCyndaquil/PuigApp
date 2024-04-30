package org.puig.puigapi.controller.responses;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.util.jackson.PuigMapper;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectResponse extends Response {
    @Builder.Default protected int quantity = 0;
    protected @JsonRawValue Object body;

    @Override
    public ResponseEntity<Response> transform() {
        try {
            quantity = body instanceof Collection<?> collection ?
                    collection.size() :
                    body.getClass().isArray() ?
                            ((Object[]) body).length :
                            1;

            ObjectMapper mapper = new PuigMapper();
            body = mapper.writeValueAsString(body);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return super.transform();
    }
}
