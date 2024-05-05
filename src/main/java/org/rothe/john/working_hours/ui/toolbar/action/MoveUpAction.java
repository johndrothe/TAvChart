package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.ui.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class MoveUpAction extends ToolbarAction {
    private final JComponent parent;

    public MoveUpAction(JComponent parent) {
        super("Move Up", Images.load("move-up.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void displayChanged(DisplayChangeEvent event) {
        setEnabled(event.isTableDisplayed());
    }
}
