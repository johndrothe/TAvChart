package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.util.ZoomHandler;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

import static java.lang.String.format;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static org.rothe.john.swc.util.Images.load;

public class ZoomAction extends ToolbarAction {
    private final JComponent parent;
    private final ZoomHandler zoomHandler;
    private final int scale;

    public ZoomAction(JComponent parent, ZoomHandler zoomHandler, int scale) {
        super(format("Zoom %d%%", scale), load("zoom.png"));
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
                parent,
                confirmMessage(),
                "Set Zoom & Exit?",
                OK_CANCEL_OPTION) == OK_OPTION;
    }

    private String confirmMessage() {
        return "Change the zoom to %d%% and exit?".formatted(scale);
    }
}
