package org.rothe.john.working_hours.event.undo;

import org.rothe.john.working_hours.event.TeamChangedEvent;
import org.rothe.john.working_hours.event.TeamListener;

import javax.swing.undo.UndoManager;

import static java.util.Objects.isNull;

public class UndoListener extends UndoManager implements TeamListener {
    public UndoListener() {
        setLimit(20);
    }

    @Override
    public void teamChanged(TeamChangedEvent e) {
        if (e.source() == this) {
            return;
        }

        if (isNull(e.oldTeam()) || isNull(e.team())) {
            discardAllEdits();
            return;
        }

        addEdit(new TeamEdit(this, e.event(), e.oldTeam(), e.team()));
    }
}
