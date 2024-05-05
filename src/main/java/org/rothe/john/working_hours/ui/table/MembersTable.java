package org.rothe.john.working_hours.ui.table;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.editors.ZoneEditor;

import javax.swing.JTable;
import java.util.List;

public class MembersTable extends JTable {

    public MembersTable(){
        super(new MembersTableModel());
        setCellSelectionEnabled(true);
        setRowHeight(26);

        setDefaultEditor(Zone.class, new ZoneEditor());
    }

    public void setMembers(List<Member> members) {
        getModel().setMembers(members);
    }

    @Override
    public MembersTableModel getModel() {
        return (MembersTableModel)super.getModel();
    }
}
