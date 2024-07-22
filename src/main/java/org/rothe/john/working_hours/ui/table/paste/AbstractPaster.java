package org.rothe.john.working_hours.ui.table.paste;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.MembersTable;

import static org.rothe.john.working_hours.model.TimePair.businessLunch;
import static org.rothe.john.working_hours.model.TimePair.businessNormal;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
class AbstractPaster {
    protected final CopiedContent content;
    protected final MembersTable table;

    protected void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, "Paste", newTeam);
    }

    protected Team applyValues(Team team,
                               CopiedContent content,
                               int startRow,
                               int startColumn) {
        PasteCursor cursor = new PasteCursor(team, startRow, startColumn);

        for (int row = 0; row < content.getRowCount(); ++row) {
            for (int column = 0; column < content.getColumnCount(); ++column) {
                cursor.setNext(content.getValueAt(row, column));
            }
            cursor.nextRow();
        }
        return cursor.team();
    }

    protected static Member newMember() {
        return new Member("", "", "",
                Zone.here(),
                businessNormal(Zone.here()),
                businessLunch(Zone.here()));
    }
}
