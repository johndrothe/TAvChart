package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.ui.util.Images;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class MoveDownAction extends AbstractAction {
    private final JComponent parent;

    public MoveDownAction(JComponent parent) {
        super("Move Up", Images.load("move-down.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
