package work.rothe.tav.ui.action;

import lombok.val;
import work.rothe.tav.ui.AboutDialog;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

public class AboutAction extends ToolbarAction {
    private final JComponent parent;

    public AboutAction(JComponent parent) {
        super("About", null);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        val dialog = new AboutDialog(SwingUtilities.windowForComponent(parent));

        dialog.setVisible(true);
    }
}
