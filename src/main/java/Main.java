import org.rothe.john.team_schedule.ui.ApplicationFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplicationFrame().setVisible(true));
    }
}
