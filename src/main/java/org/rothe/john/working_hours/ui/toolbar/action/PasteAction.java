package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.ui.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public class PasteAction extends ToolbarAction {
    private final JComponent parent;

    public PasteAction(JComponent parent) {
        super("Paste", Images.load("paste.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void displayChanged(DisplayChangeEvent event) {
        setEnabled(event.isTableDisplayed());
    }
}
