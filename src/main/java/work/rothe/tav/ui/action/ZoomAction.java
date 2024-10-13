package work.rothe.tav.ui.action;

import work.rothe.tav.util.ZoomHandler;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

import static java.lang.String.format;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static work.rothe.tav.util.Images.load;

public class ZoomAction extends ToolbarAction {
    private final JComponent parent;
    private final ZoomHandler zoomHandler;
    private final int scale;

    public ZoomAction(JComponent parent, ZoomHandler zoomHandler, int scale) {
        super(format("Zoom %d%%", scale), load("zoom"));
        this.parent = parent;
        this.zoomHandler = zoomHandler;
        this.scale = scale;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(confirm()) {
            zoomHandler.set(scale);
        }
    }

    private boolean confirm() {
        return showConfirmDialog(
                SwingUtilities.windowForComponent(parent),
                confirmMessage(),
                "Set Zoom & Exit?",
                OK_CANCEL_OPTION) == OK_OPTION;
    }

    private String confirmMessage() {
        return "Change the zoom to %d%% and exit?".formatted(scale);
    }
}
