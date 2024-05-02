package org.puig.puigapi.util;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@AllArgsConstructor
public enum Fonts {
    ANTON(Objects.requireNonNull(Fonts.class.getClassLoader().getResource(
            "font/anton-latin-400-normal.ttf"))),
    LUCKIES_GUY(Objects.requireNonNull(Fonts.class.getClassLoader().getResource(
            "font/luckiest-guy-latin-400-normal.ttf")));

    private final URL url;

    public static Font loadFont(@NonNull Fonts font) {
        try {
            InputStream inputStream = font.url.openStream();
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
