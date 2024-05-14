package org.puig.api.persistence.entity.finances;

import jakarta.validation.constraints.*;
import lombok.*;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.util.Articulo;
import org.puig.api.util.contable.Contable;
import org.puig.api.util.contable.Detalle;
import org.puig.api.util.grupos.InitInfo;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

/**
 * Representa un producto que se encuentra en el menú. Producto o artículo con precio.
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class ArticuloMenu extends Articulo {
    @NotNull(message = "Se requiere la categoria del artículo de menu",
            groups = InitInfo.class)
    private Categorias categoria;
    private String codigo_barra;
    /**
     * Contiene los Productos de Sucursal que se usarán para la elaboración de este Artículo
     */
    @NotNull(message = "Se requiere una receta al menos vacía",
            groups = InitInfo.class)
    private Detalle<Contable<Proveedor.Producto>> receta = new Detalle<>();
    private Detalle<Contable<ArticuloMenu>> adicionales = new Detalle<>();

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
