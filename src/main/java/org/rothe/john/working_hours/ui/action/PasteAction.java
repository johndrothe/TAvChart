package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.util.Images;

import java.awt.event.ActionEvent;

public class PasteAction extends ToolbarAction {
    private final MembersTable table;

    public PasteAction(MembersTable table) {
        super("Paste", Images.load("paste.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
