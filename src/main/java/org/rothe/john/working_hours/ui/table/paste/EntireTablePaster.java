package org.rothe.john.working_hours.ui.table.paste;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.table.MembersTable;

import java.util.ArrayList;
import java.util.List;

class EntireTablePaster extends AbstractPaster {
    EntireTablePaster(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new EntireTablePaster(content, table).paste();
    }

    private void paste() {
        Team team = table.getTeam().withMembers(newMembers());
        team = applyValues(team, content, 0, 0);
        fireTeamChanged(team);
    }

    private List<Member> newMembers() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < content.getRowCount(); ++i) {
            members.add(newMember());
        }
        return members;
    }
}
