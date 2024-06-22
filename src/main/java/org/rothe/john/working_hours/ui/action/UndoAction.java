package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.event.TeamChangedEvent;
import org.rothe.john.working_hours.event.TeamListener;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.event.undo.UndoListener;
import org.rothe.john.working_hours.util.Images;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoAction extends ToolbarAction implements TeamListener {
    private final UndoListener listener;

    public UndoAction(UndoListener listener) {
        super("Undo", Images.load("undo.png"));
        this.listener = listener;

        Teams.addTeamListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.undo();
    }

    @Override
    public void teamChanged(TeamChangedEvent event) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    private void updateDisplay() {
        putValue(SHORT_DESCRIPTION, listener.getUndoPresentationName());
        setEnabled(listener.canUndo());
    }
}
