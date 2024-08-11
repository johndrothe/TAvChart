package org.rothe.john.swc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Setter;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Data
@Setter
public class Settings {
    private Dimension mainWindowSize = new Dimension(1024, 768);
    private int uiScale = 1;


    public static Path getSettingsPath() {
        return Path.of(System.getProperty("user.home"), ".swh_settings");
    }

    public void save() {
        save(getSettingsPath());
    }

    public void save(Path target) {
        try {
            Files.writeString(target, newGson().toJson(this), UTF_8, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error: Failed to save user-preferences to " + target);
            e.printStackTrace();
        }
    }

    public static Settings load() {
        return load(getSettingsPath());
    }

    public static Settings load(Path target) {
        try {
            if (Files.exists(target)) {
                return newGson().fromJson(Files.readString(target, UTF_8), Settings.class);
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load user-preferences from " + target);
            e.printStackTrace();
        }
        return new Settings();
    }

    private static Gson newGson() {
        return new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }
}
