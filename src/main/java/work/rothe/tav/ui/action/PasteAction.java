package work.rothe.tav.ui.action;

import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.Paster;
import work.rothe.tav.util.Images;

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
