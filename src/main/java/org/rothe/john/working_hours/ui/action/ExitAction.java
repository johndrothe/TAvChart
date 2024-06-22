package org.rothe.john.working_hours.ui.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.*;

public class ExitAction extends AbstractAction {
    private final JComponent parent;
    public ExitAction(JComponent parent) {
        super("Exit");
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(confirm()) {
            System.exit(0);
        }
    }
    private boolean confirm() {
        return showConfirmDialog(parent,
                "Exit?",
                "Exit",
                YES_NO_OPTION) == YES_OPTION;
    }
}
