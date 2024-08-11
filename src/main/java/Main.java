import org.rothe.john.working_hours.ui.ApplicationFrame;
import org.rothe.john.working_hours.util.Settings;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Settings settings = Settings.load();
        applySettings(settings);
        SwingUtilities.invokeLater(() -> new ApplicationFrame(settings).setVisible(true));
    }

    private static void applySettings(Settings settings) {
        System.setProperty("sun.java2d.uiScale", Integer.toString(settings.getUiScale()));
    }
}
