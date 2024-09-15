package work.rothe.tav.ui.table.paste.operations;

import work.rothe.tav.model.Member;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.CopiedContent;

import java.util.ArrayList;
import java.util.List;

public class EntireTableOperation extends AbstractPasteOperation {
    EntireTableOperation(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new EntireTableOperation(content, table).paste();
    }

    private void paste() {
        Document document = table.getDocument().withMembers(newMembers());
        document = applyValues(document, content, 0, 0);
        fireDocumentChanged(document);
    }

    private List<Member> newMembers() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < content.getRowCount(); ++i) {
            members.add(newMember());
        }
        return members;
    }
}
