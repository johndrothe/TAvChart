package work.rothe.tav.util;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.jar.Manifest;

import static java.util.Objects.nonNull;

public class ManifestUtil {
    private static final String VERSION = "ApplicationVersion";
    private static final String DATE = "ApplicationBuildDate";
    private static final String WEBSITE = "WebsiteURL";

    private ManifestUtil() {
    }

    public static String getVersion() {
        return getProperty(VERSION);
    }

    public static String getBuildDate() {
        return getProperty(DATE);
    }

    public static String getWebsite() {
        return getProperty(WEBSITE);
    }

    public static String getProperty(String name) {
        return readManifest()
                .map(Manifest::getMainAttributes)
                .map(a -> a.getValue(name))
                .orElse("n/a");
    }

    private static Optional<Manifest> readManifest() {
        try {
            URL resource = ManifestUtil.class.getClassLoader().getResource("META-INF/MANIFEST.MF");
            if (nonNull(resource)) {
                return Optional.of(new Manifest(resource.openStream()));
            }
        } catch (IOException E) {
            // handle
        }
        return Optional.empty();
    }

}
