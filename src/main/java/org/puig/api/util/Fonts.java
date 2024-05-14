package org.puig.api.util;

import lombok.AllArgsConstructor;

import java.net.URL;
import java.util.Objects;

@AllArgsConstructor
public enum Fonts {
    ANTON(Objects.requireNonNull(Fonts.class.getClassLoader().getResource(
            "font/anton-latin-400-normal.ttf"))),
    LUCKIES_GUY(Objects.requireNonNull(Fonts.class.getClassLoader().getResource(
            "font/luckiest-guy-latin-400-normal.ttf")));

    private final URL url;

}
