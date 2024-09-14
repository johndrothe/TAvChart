package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.ui.ApplicationFrame;

import java.awt.event.ActionEvent;

import static java.lang.String.format;
import static org.rothe.john.swc.util.Images.load;

public class ZoomAction extends ToolbarAction {
    private final ApplicationFrame frame;
    private final int uiScale;

    public ZoomAction(ApplicationFrame frame, int uiScale) {
        super(format("Zoom %d%%", uiScale), load("zoom.png"));
        this.frame = frame;
        this.uiScale = uiScale;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setUiScale(uiScale);
    }
}
