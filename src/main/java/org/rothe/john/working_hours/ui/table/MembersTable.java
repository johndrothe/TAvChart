package org.rothe.john.working_hours.ui.table;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.editors.LocalTimeEditor;
import org.rothe.john.working_hours.ui.table.editors.ZoneEditor;

import javax.swing.JTable;
import java.time.LocalTime;
import java.util.Optional;

public class MembersTable extends JTable {
    private Team team;
    public MembersTable(){
        super(new MembersTableModel());
        setCellSelectionEnabled(true);
        setRowHeight(26);

        setDefaultEditor(LocalTime.class, new LocalTimeEditor());
        setDefaultEditor(Zone.class, new ZoneEditor());
    }

    public void setTeam(Team team) {
        this.team = team;
        getModel().setTeam(team);
        repaint();
    }

    @Override
    public MembersTableModel getModel() {
        return (MembersTableModel)super.getModel();
    }

    public Optional<Member> getSelectedMember() {
        val index = getSelectedRow();
        if (index >= 0 && index <= team.getMembers().size()) {
            return Optional.of(team.getMembers().get(index));
        }
        return Optional.empty();
    }
}
