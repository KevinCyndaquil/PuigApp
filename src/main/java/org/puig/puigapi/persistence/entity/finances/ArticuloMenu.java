package org.puig.puigapi.persistence.entity.finances;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.util.Articulo;
import org.puig.puigapi.util.contable.Contable;
import org.puig.puigapi.util.contable.Detalle;
import org.puig.puigapi.util.persistence.Instantiator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

/**
 * Representa un producto que se encuentra en el menú. Producto o artículo con precio.
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class ArticuloMenu extends Articulo {
    private Categorias categoria;
    /**
     * Contiene los Productos de Sucursal que se usarán para la elaboración de este Artículo
     */
    private Detalle<Contable<Proveedor.Producto>> receta = new Detalle<>();
    private Detalle<Contable<ArticuloMenu>> adicionales = new Detalle<>();

    @Data
    public static class PostRequest implements Instantiator<ArticuloMenu> {
        @NotBlank(message = "Código del artículo de menú no válido")
        private String id;
        @NotBlank(message = "Nombre del artículo de menú no válido")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre incorrecto, recuerda usar solo mayúsculas y dígitos")
        private String nombre;
        @Positive(message = "El precio del articulo debe ser mayor a cero")
        private double precio;
        private boolean visible = true;
        @NotNull(message = "Se requiere la categoria del artículo de menu")
        private Categorias categoria;
        @NotNull(message = "Se requiere una receta al menos vacía")
        private Detalle<Contable<Proveedor.Producto>> receta = new Detalle<>();
        private Detalle<Contable<ArticuloMenu>> adicionales = new Detalle<>();

        @Override
        public ArticuloMenu instance() {
            return ArticuloMenu.builder()
                    .id(id)
                    .nombre(nombre)
                    .precio(precio)
                    .visible(visible)
                    .categoria(categoria)
                    .receta(receta)
                    .adicionales(adicionales)
                    .build();
        }
    }

    @Override
    public Especializaciones getEspecializado() {
        return Especializaciones.ARTICULO_MENU;
    }

    @Override
    public boolean isEn_desabasto(Sucursal sucursal) {
        en_desabasto = receta.stream()
                .map(Contable::getDetalle)
                .map(a -> sucursal.getBodega().getExistencias(a) < 5)
                .reduce(false, Boolean::logicalOr);
        return en_desabasto;
    }

    @Override
    public Detalle<Contable<Proveedor.Producto>> getContables() {
        Detalle<Contable<Proveedor.Producto>> contenido = new Detalle<>(receta);
        contenido.addAll(receta);
        contenido.addAll(adicionales.stream()
                .map(Contable::getDetalle)
                .map(ArticuloMenu::getReceta)
                .flatMap(Collection::stream)
                .toList());
        return contenido;
    }
}
