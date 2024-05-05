package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.ui.util.Images;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class PasteAction extends AbstractAction {
    private final JComponent parent;

    public PasteAction(JComponent parent) {
        super("Paste", Images.load("paste.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
