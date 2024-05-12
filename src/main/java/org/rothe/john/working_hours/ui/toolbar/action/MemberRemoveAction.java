package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.util.Images;

import java.awt.event.ActionEvent;

public class MemberRemoveAction extends ToolbarAction {
    private final MembersTable table;

    public MemberRemoveAction(MembersTable table) {
        super("Remove Member", Images.load("xmark-square.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}
