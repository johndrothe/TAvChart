import com.github.swingdpi.UiDefaultsScaler;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.ApplicationFrame;
import work.rothe.tav.util.Settings;

import javax.swing.SwingUtilities;
import java.util.List;

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
