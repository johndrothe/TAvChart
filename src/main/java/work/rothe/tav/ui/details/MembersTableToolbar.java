package work.rothe.tav.ui.details;

import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.ui.action.CopyAction;
import work.rothe.tav.ui.action.ExportCsvAction;
import work.rothe.tav.ui.action.ImportCsvAction;
import work.rothe.tav.ui.action.MemberAddAction;
import work.rothe.tav.ui.action.MemberRemoveAction;
import work.rothe.tav.ui.action.MoveDownAction;
import work.rothe.tav.ui.action.MoveUpAction;
import work.rothe.tav.ui.action.PasteAction;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.Paster;

import javax.swing.JToolBar;

public class MembersTableToolbar extends JToolBar {
    private final MembersTable table;
    private final CopyAction copyAction;
    private final PasteAction pasteAction;

    public MembersTableToolbar(MembersTable table) {
        super();
        this.table = table;
        this.copyAction = new CopyAction(table);
        this.pasteAction = new PasteAction(table);

        setFloatable(false);
        createButtons();
    }

    public void documentChanged(DocumentChangedEvent event) {
        copyAction.setEnabled(table.getRowCount() > 0);
        pasteAction.setEnabled(Paster.canPaste(table));
    }

    private void createButtons() {
        add(copyAction).setEnabled(table.getRowCount() > 0);
        add(pasteAction).setEnabled(Paster.canPaste(table));

        addSeparator();

        add(new MemberAddAction(table));
        add(new MemberRemoveAction(table));

        addSeparator();
        add(new MoveUpAction(table));
        add(new MoveDownAction(table));

        addSeparator();
        add(new ImportCsvAction(table));
        add(new ExportCsvAction(table));
    }
}
