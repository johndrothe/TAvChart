package work.rothe.tav.util;

import org.junit.jupiter.api.Test;

import javax.swing.ImageIcon;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ImagesTest {
    private static final String ICON_NAME = "copy";

    @Test
    void testIconsByUiScale() {
        uiScales().forEach(this::assertIconByUiScale);
    }

    private void assertIconByUiScale(int uiScale) {
        Images.initialize(settings(uiScale));
        assertIconByUiScale(Images.load(ICON_NAME), uiScale);
    }

    private void assertIconByUiScale(ImageIcon image, int uiScale) {
        assertTrue(image.getDescription().endsWith("zoom_%d/%s.png".formatted(uiScale, ICON_NAME)));
    }

    private Settings settings(int uiScale) {
        Settings settings = new Settings();
        settings.setUiScale(uiScale);
        return settings;
    }

    private static List<Integer> uiScales() {
        return List.of(100, 125, 150, 200, 250, 300);
    }
}