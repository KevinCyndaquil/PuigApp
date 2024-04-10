package org.puig.puigapi.persistence.entity.utils.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;

import java.io.IOException;

public class PuigMapper extends ObjectMapper {
    public PuigMapper() {
        registerModule(new JavaTimeModule());
        registerModule(ventaModule());
        setSerializationInclusion(Include.NON_NULL);
    }

    protected SimpleModule ventaModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Venta.class, new VentaDeserializer());
        return module;
    }

    public static class VentaDeserializer extends JsonDeserializer<Venta> {

        @Override
        public Venta deserialize(@NotNull JsonParser parser, DeserializationContext ctxt)
                throws IOException {
            ObjectMapper mapper = (ObjectMapper) parser.getCodec();
            Venta venta = mapper.readValue(parser, Venta.class);
            if (venta.esValida())
                return venta;

            throw new IllegalArgumentException(
                    "Deserialización de venta_request invalida. Según esValida() dentro de %s"
                            .formatted(Venta.class));
        }
    }
}
