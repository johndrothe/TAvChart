package org.rothe.john.working_hours.event.undo;

import org.rothe.john.working_hours.event.TeamChangedEvent;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Team;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

record TeamEdit(UndoListener listener, String change, Team oldTeam, Team newTeam)
        implements UndoableEdit {

    @Override
    public void undo() throws CannotUndoException {
        Teams.fireTeamChanged(new TeamChangedEvent(listener, getUndoPresentationName(), newTeam, oldTeam));
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo() throws CannotRedoException {
        Teams.fireTeamChanged(new TeamChangedEvent(listener, getRedoPresentationName(), oldTeam, newTeam));
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {

    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return change;
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo " + change;
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo " + change;
    }
}
