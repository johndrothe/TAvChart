package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class RedoAction extends ToolbarAction {
    private final JComponent parent;

    public RedoAction(JComponent parent) {
        super("Redo", Images.load("redo.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
