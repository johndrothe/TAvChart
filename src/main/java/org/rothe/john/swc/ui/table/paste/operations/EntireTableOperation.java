package org.rothe.john.swc.ui.table.paste.operations;

import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.ui.table.MembersTable;
import org.rothe.john.swc.ui.table.paste.CopiedContent;

import java.util.ArrayList;
import java.util.List;

public class EntireTableOperation extends AbstractPasteOperation {
    EntireTableOperation(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new EntireTableOperation(content, table).paste();
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
