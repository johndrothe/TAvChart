package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.ui.table.MembersTable;
import org.rothe.john.swc.util.Images;

import java.awt.event.ActionEvent;

public class CopyAction extends ToolbarAction {
    private final MembersTable table;

    public CopyAction(MembersTable table) {
        super("Copy", Images.load("copy.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "copy");
        table.getActionMap().get(event.getActionCommand()).actionPerformed(event);
    }
}
