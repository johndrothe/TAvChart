package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.util.Images;

import java.awt.event.ActionEvent;

public class CopyAction extends ToolbarAction {
    private final MembersTable table;

    public CopyAction(MembersTable table) {
        super("Copy", Images.load("copy.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
