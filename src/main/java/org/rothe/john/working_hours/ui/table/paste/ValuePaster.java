package org.rothe.john.working_hours.ui.table.paste;

import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.table.MembersTable;

class ValuePaster extends AbstractPaster {
    private ValuePaster(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new ValuePaster(content, table).paste();
    }

    private void paste() {
        // Data > Selection: Start pasting at the first selected cell.
        // TODO: Data <= Selection: Paste one or more complete copies of the data into the selected cells
        Team team = applyValues(table.getTeam(), content, table.getSelectedRow(), table.getSelectedColumn());

        Teams.fireTeamChanged(this, "Paste", team);
    }

}
