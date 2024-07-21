package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.table.util.MemberRemover;
import org.rothe.john.working_hours.util.Images;

import java.awt.event.ActionEvent;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

        fireTeamChanged(MemberRemover.remove(team, indexes));
    }

    private void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, String.valueOf(getValue(NAME)), newTeam);
    }
}
