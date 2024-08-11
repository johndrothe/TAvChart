package org.rothe.john.swc.util;

import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Optional;

public abstract class Images {
    private Images() {

    }

    public static ImageIcon load(String name) {
        return toResource(name).map(ImageIcon::new).orElse(null);
    }

    private static Optional<URL> toResource(String name) {
        return Optional.ofNullable(Images.class.getResource("/images/" + name));
    }
}
