package work.rothe.tav.util;

import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class Images {
    private static final AtomicReference<Images> instance = new AtomicReference<>();
    private final int uiScale;

    private Images(Settings settings) {
        this.uiScale = settings.getUiScale();
    }

    public static void initialize(Settings settings) {
        instance.set(new Images(settings));
    }

    public static ImageIcon load(String name) {
        return toResource(instance.get().location(name))
                .map(ImageIcon::new)
                .orElse(null);
    }

    private String location(String name) {
        return "zoom_%d/%s.png".formatted(uiScale, name);
    }

    private static Optional<URL> toResource(String name) {
        return Optional.ofNullable(Images.class.getResource("/images/" + name));
    }
}
