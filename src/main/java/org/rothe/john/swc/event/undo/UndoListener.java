package org.rothe.john.swc.event.undo;

import org.rothe.john.swc.event.NewTeamEvent;
import org.rothe.john.swc.event.TeamChangedEvent;
import org.rothe.john.swc.event.TeamListener;

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

        if (shouldDiscardEdits(e)) {
            discardAllEdits();
            return;
        }

        addEdit(new TeamEdit(this, e.event(), e.oldTeam(), e.team()));
    }

    private static boolean shouldDiscardEdits(TeamChangedEvent e) {
        return e instanceof NewTeamEvent || isNull(e.oldTeam()) || isNull(e.team());
    }
}
