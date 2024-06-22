package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.event.TeamChangedEvent;
import org.rothe.john.working_hours.event.TeamListener;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.event.undo.UndoListener;
import org.rothe.john.working_hours.util.Images;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoAction extends ToolbarAction implements TeamListener {
    private final UndoListener listener;

    public RedoAction(UndoListener listener) {
        super("Redo", Images.load("redo.png"));
        this.listener = listener;

        Teams.addTeamListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.redo();
    }

    @Override
    public void teamChanged(TeamChangedEvent event) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    private void updateDisplay() {
        putValue(SHORT_DESCRIPTION, listener.getRedoPresentationName());
        setEnabled(listener.canRedo());
    }
}
