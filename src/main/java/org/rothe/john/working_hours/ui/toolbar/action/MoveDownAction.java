package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.ui.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class MoveDownAction extends ToolbarAction {
    private final JComponent parent;

    public MoveDownAction(JComponent parent) {
        super("Move Up", Images.load("move-down.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
