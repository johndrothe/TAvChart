package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class UndoAction extends ToolbarAction {
    private final JComponent parent;

    public UndoAction(JComponent parent) {
        super("Undo", Images.load("undo.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
