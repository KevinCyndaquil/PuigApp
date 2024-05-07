package org.puig.puigapi.util.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;

import java.io.IOException;

public class PuigMapper extends ObjectMapper {
    public PuigMapper() {
        registerModule(new JavaTimeModule());

        registerModule(ventaModule());
        registerModule(objectIdModule());

        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    }

    protected SimpleModule ventaModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Venta.class, new VentaDeserializer());
        return module;
    }

    protected SimpleModule objectIdModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ObjectId.class, new JsonDeserializer<>() {
            @Override
            public ObjectId deserialize(JsonParser parser, DeserializationContext ctxt)
                    throws IOException {
                System.out.println(parser.getText());
                String objectIdStr = parser.getText();
                return new ObjectId(objectIdStr);
            }
        });
        module.addSerializer(ObjectId.class, new JsonSerializer<>() {
            @Override
            public void serialize(ObjectId objectId, JsonGenerator generator, SerializerProvider provider)
                    throws IOException {
                String objectIdStr = objectId.toHexString();
                generator.writeString(objectIdStr);
            }
        });
        return module;
    }

    public static class VentaDeserializer extends JsonDeserializer<Venta> {

        @Override
        public Venta deserialize(@NotNull JsonParser parser, DeserializationContext ctxt)
                throws IOException {
            ObjectMapper mapper = (ObjectMapper) parser.getCodec();
            Venta venta = mapper.readValue(parser, Venta.class);
            venta.validar();

            throw new IllegalArgumentException(
                    "Deserialización de venta_request invalida. Según esValida() dentro de %s"
                            .formatted(Venta.class));
        }
    }
}
