package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.ui.table.MembersTable;
import org.rothe.john.swc.ui.table.paste.Paster;
import org.rothe.john.swc.util.Images;

import java.awt.event.ActionEvent;

public class PasteAction extends ToolbarAction {
    private final MembersTable table;

    public PasteAction(MembersTable table) {
        super("Paste", Images.load("paste.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Paster.paste(table);
    }
}
