package org.rothe.john.swc.util;

import com.github.swingdpi.DpiUtils;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rothe.john.swc.io.json.Json;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Data
@Setter
@Slf4j
public class Settings {
    private Dimension mainWindowSize = new Dimension(1024, 768);
    private int uiScale = DpiUtils.getClosestStandardScaling();


    public static Path getSettingsPath() {
        return Path.of(System.getProperty("user.home"), ".swh_settings");
    }

    public void save() {
        save(getSettingsPath());
    }

    public void save(Path target) {
        try {
            Files.writeString(target, Json.toJson(this), UTF_8, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to save user-preferences to: {}", target, e);
        }
    }

    public static Settings load() {
        return load(getSettingsPath());
    }

    public static Settings load(Path target) {
        try {
            if (Files.exists(target)) {
                return Json.fromJson(Files.readString(target, UTF_8), Settings.class);
            }
        } catch (Throwable e) {
            log.error("Failed to load user-preferences from: {}", target, e);
        }
        return new Settings();
    }

    public static Dimension minimum(Dimension left, Dimension right) {
        return new Dimension(
                Math.min(right.width, left.width),
                Math.min(right.height, left.height)
        );
    }
}
