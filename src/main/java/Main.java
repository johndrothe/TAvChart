import com.github.swingdpi.UiDefaultsScaler;
import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.ApplicationFrame;
import org.rothe.john.swc.util.Settings;

import javax.swing.SwingUtilities;
import java.util.List;

// TODO: UI Scaling List
// TODO: Update canvas rendering to use global scaling instead of an arbitrary 30 pixels
// TODO: Switch to SVG and/or add icon sizes for each global scaling option
// TODO: Evaluate how sun.java2d.uiScale interacts with SwingDPI


public class Main {
    public static void main(String[] args) {
        Settings settings = Settings.load();
        applySettings(settings);
        SwingUtilities.invokeLater(() -> new ApplicationFrame(settings).setVisible(true));
        Documents.fireDocumentChanged(Main.class, "New", new Document("Winners", 0, List.of()));

        Runtime.getRuntime().addShutdownHook(new Thread(settings::save));
    }

    private static void applySettings(Settings settings) {
        UiDefaultsScaler.updateAndApplyGlobalScaling(settings.getUiScale(), true);
    }
}
