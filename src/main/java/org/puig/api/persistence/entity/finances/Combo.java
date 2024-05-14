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

import java.time.LocalDate;
import java.util.Collection;

/**
 * Conjunto de artículos que se encuentran en el menú que por consiguente traen un descuento
 * o son considerados una promoción.
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class Combo extends Articulo {
    @NotEmpty(message = "Contenido del combo o promoción no válido",
            groups = InitInfo.class)
    private Detalle<Contable<ArticuloMenu>> contenido;
    @NotNull(message = "Se requiere la fecha de inicio del combo",
            groups = InitInfo.class)
    private LocalDate inicia;
    private LocalDate vigencia;

    @Override
    public Especializaciones getEspecializado() {
        return Especializaciones.ARTICULO_COMBO;
    }

    @Override
    public boolean isEn_desabasto(Sucursal sucursal) {
        en_desabasto = contenido.stream()
                .map(Contable::getDetalle)
                .map(a -> a.isEn_desabasto(sucursal))
                .reduce(false, Boolean::logicalOr);
        return en_desabasto;
    }

    @Override
    public Detalle<Contable<Proveedor.Producto>> getContables() {
        return contenido.stream()
                .map(Contable::getDetalle)
                .map(ArticuloMenu::getContables)
                .flatMap(Collection::stream)
                .collect(Detalle::new, Detalle::add, Detalle::addAll);
    }
}