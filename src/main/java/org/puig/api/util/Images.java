package org.puig.api.util;

import java.net.URL;
import java.util.Objects;

public enum Images {
    LOGO_PUIGAPP(Objects.requireNonNull(Images.class.getClassLoader().getResource(
            "image/icon/logo_puigapp.png"))),
    LOGO_POLLOSPUIG(Objects.requireNonNull(Images.class.getClassLoader().getResource(
            "image/icon/logo_pollospuig.png")));

    public final URL url;

    Images(URL url) {
        this.url = url;
    }
}
