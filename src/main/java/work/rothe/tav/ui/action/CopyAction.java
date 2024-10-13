package work.rothe.tav.ui.action;

import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.Images;

import java.awt.event.ActionEvent;

public class CopyAction extends ToolbarAction {
    private final MembersTable table;

    public CopyAction(MembersTable table) {
        super("Copy", Images.load("copy"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "copy");
        table.getActionMap().get(event.getActionCommand()).actionPerformed(event);
    }
}
