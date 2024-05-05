package org.rothe.john.working_hours.ui.table;

import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.editors.LocalTimeEditor;
import org.rothe.john.working_hours.ui.table.editors.ZoneEditor;

import javax.swing.JTable;
import java.time.LocalTime;

public class MembersTable extends JTable {

    public MembersTable(){
        super(new MembersTableModel());
        setCellSelectionEnabled(true);
        setRowHeight(26);

        setDefaultEditor(LocalTime.class, new LocalTimeEditor());
        setDefaultEditor(Zone.class, new ZoneEditor());
    }

    public void setTeam(Team team) {
        getModel().setTeam(team);
    }

    @Override
    public MembersTableModel getModel() {
        return (MembersTableModel)super.getModel();
    }
}
