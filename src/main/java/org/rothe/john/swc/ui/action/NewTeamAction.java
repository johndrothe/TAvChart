package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.util.List;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

public class NewTeamAction extends ToolbarAction {
    private final JComponent parent;

    public NewTeamAction(JComponent parent) {
        super("New Team", Images.load("new.png"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (confirm()) {
            Teams.fireNewTeam(this, "New Team", new Team("New Team", List.of()));
        }
    }

    private boolean confirm() {
        return showConfirmDialog(parent,
                "Create New Team",
                "Create a new empty team?",
                YES_NO_OPTION) == YES_OPTION;
    }
}
