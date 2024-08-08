package org.rothe.john.working_hours.ui.table.paste.operations;

import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.table.paste.CopiedContent;
import org.rothe.john.working_hours.util.MemberRemover;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RowOperation extends AbstractPasteOperation {
    private RowOperation(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new RowOperation(content, table).paste();
    }

    private void paste() {
        // Replace the selected rows with the rows in the data.
        Team team = addOrRemoveRows(table.getTeam(), content);
        // Paste in the values in the copied content.
        team = applyValues(team, content, table.getSelectedRow(), 0);
        Teams.fireTeamChanged(this, "Paste", team);
    }

    private Team addOrRemoveRows(Team team, CopiedContent content) {
        return addRequiredRows(removeExcessRows(team, content), content);
    }

    private Team addRequiredRows(Team team, CopiedContent content) {
        return team.withMembers(toList(membersBeforeNew(team), newMembers(content), membersAfterNew(team)));
    }

    private Stream<Member> membersBeforeNew(Team team) {
        return team.getMembers().stream().limit(table.getLastSelectedRow());
    }

    private Stream<Member> membersAfterNew(Team team) {
        return team.getMembers().stream().skip(table.getLastSelectedRow());
    }

    private Stream<Member> newMembers(CopiedContent content) {
        return IntStream.range(0, (content.getRowCount() - table.getSelectedRowCount()))
                .mapToObj(unused -> newMember());
    }

    private Team removeExcessRows(Team team, CopiedContent data) {
        return MemberRemover.remove(team, getRowsToRemove(data));
    }

    private int[] getRowsToRemove(CopiedContent data) {
        return IntStream.of(table.getSelectedRows())
                .sorted()
                .skip(data.getRowCount())
                .toArray();
    }

    private static List<Member> toList(Stream<Member> s1,
                                       Stream<Member> s2,
                                       Stream<Member> s3) {
        return Stream.concat(Stream.concat(s1, s2), s3).toList();
    }
}
