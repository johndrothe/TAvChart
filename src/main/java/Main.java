import org.rothe.john.working_hours.ui.ApplicationFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplicationFrame().setVisible(true));
    }
}
