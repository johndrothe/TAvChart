package org.rothe.john.working_hours.ui.table;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.editors.LocalTimeEditor;
import org.rothe.john.working_hours.ui.table.editors.ZoneEditor;

import javax.swing.*;
import java.time.LocalTime;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class MembersTable extends JTable {
    private Team team;

    public MembersTable() {
        super(new MembersTableModel());
        updateTableConfiguration();

        initializeEditors();
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        getModel().setTeam(team);

        if(nonNull(team)) {
            SwingUtilities.invokeLater(()-> ColumnSizing.adjust(MembersTable.this));
        }
        repaint();
    }

    @Override
    public MembersTableModel getModel() {
        return (MembersTableModel) super.getModel();
    }

    public Optional<Member> getSelectedMember() {
        val index = getSelectedRow();
        if (index >= 0 && index <= team.getMembers().size()) {
            return Optional.of(team.getMembers().get(index));
        }
        return Optional.empty();
    }

    private void updateTableConfiguration() {
        setCellSelectionEnabled(true);
        setRowHeight(26);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getTableHeader().setReorderingAllowed(false);
    }

    private void initializeEditors() {
        setDefaultEditor(LocalTime.class, new LocalTimeEditor());
        setDefaultEditor(Zone.class, new ZoneEditor());
    }
}
