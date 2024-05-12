package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.util.Images;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

public class MemberRemoveAction extends ToolbarAction {
    private final MembersTable table;

    public MemberRemoveAction(MembersTable table) {
        super("Remove Member", Images.load("xmark-square.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(nonNull(table.getCellEditor())) {
            table.getCellEditor().cancelCellEditing();
        }
        removeMembers(Teams.getTeam(), table.getSelectedRows());
    }

    private void removeMembers(Team team, int[] indexes) {
        if (isNull(indexes) || indexes.length == 0) {
            return;
        }

        fireTeamChanged(team.withMembers(removeMembers(team.getMembers(), indexes)));
    }

    private List<Member> removeMembers(List<Member> members, int[] indexes) {
        return removeMembers(members, getSelected(members, indexes));
    }

    private List<Member> removeMembers(List<Member> members, Set<Member> selected) {
        return members.stream().filter(not(selected::contains)).toList();
    }

    private Set<Member> getSelected(List<Member> members, int[] indexes) {
        return IntStream.of(indexes)
                .filter(index -> index >= 0)
                .filter(index -> index < members.size())
                .mapToObj(members::get)
                .collect(Collectors.toSet());
    }

    private void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, String.valueOf(getValue(NAME)), newTeam);
    }
}
