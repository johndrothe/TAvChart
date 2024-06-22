package org.rothe.john.working_hours.ui.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

public class ExitAction extends AbstractAction {
    private final JComponent parent;
    public ExitAction(JComponent parent) {
        super("Exit");
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(showConfirmDialog(parent, "Exit?") == YES_OPTION) {
            System.exit(0);
        }
    }
}
