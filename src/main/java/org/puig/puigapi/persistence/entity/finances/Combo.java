package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.Articulo;
import org.puig.puigapi.persistence.entity.utils.DetalleDe;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

/**
 * Conjunto de artículos que se encuentran en el menú que por consiguente traen un descuento
 * o son considerados una promoción.
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class Combo extends Articulo {
    private Set<DetalleDe<ArticuloMenu>> contenido;
    private LocalDate inicia;
    private LocalDate vigencia;

    @Override
    public Especializaciones getEspecializado() {
        return Especializaciones.COMBO;
    }

    @Data
    public static class Request implements PostEntity<Combo> {
        @NotBlank(message = "Código del combo o promoción de menú no válido")
        private String codigo;
        @NotBlank(message = "Nombre del combo o promoción de menú no válido")
        private String nombre;
        @PositiveOrZero(message = "El precio del combo o promición debe ser mayor o igual a cero")
        private double precio;
        private boolean visible;
        @NotEmpty(message = "Contenido del combo o promoción no válido")
        private Set<DetalleDe<ArticuloMenu>> contenido;
        @NotNull(message = "Se requiere la fecha de inicio del combo")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate inicia;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate vigencia;

        @Override
        public Combo instance() {
            return Combo.builder()
                    .codigo(codigo)
                    .nombre(nombre)
                    .precio(precio)
                    .visible(visible)
                    .contenido(contenido)
                    .inicia(inicia)
                    .vigencia(vigencia)
                    .build();
        }
    }
}