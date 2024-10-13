package work.rothe.tav.ui.action;

import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.MemberRemover;
import work.rothe.tav.util.Images;

import java.awt.event.ActionEvent;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class MemberRemoveAction extends ToolbarAction {
    private final MembersTable table;

    public MemberRemoveAction(MembersTable table) {
        super("Remove Member", Images.load("xmark-square"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(nonNull(table.getCellEditor())) {
            table.getCellEditor().cancelCellEditing();
        }
        removeMembers(Documents.getCurrent(), table.getSelectedRows());
    }

    private void removeMembers(Document document, int[] indexes) {
        if (isNull(indexes) || indexes.length == 0) {
            return;
        }

        fireDocumentChanged(MemberRemover.remove(document, indexes));
    }

    private void fireDocumentChanged(Document newDocument) {
        Documents.fireDocumentChanged(this, String.valueOf(getValue(NAME)), newDocument);
    }
}
