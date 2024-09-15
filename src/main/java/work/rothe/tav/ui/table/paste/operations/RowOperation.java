package work.rothe.tav.ui.table.paste.operations;

import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.CopiedContent;
import work.rothe.tav.util.MemberRemover;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RowOperation extends AbstractPasteOperation {
    private RowOperation(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new RowOperation(content, table).paste();
    }

    private void paste() {
        // Replace the selected rows with the rows in the data.
        Document document = addOrRemoveRows(table.getDocument(), content);
        // Paste in the values in the copied content.
        document = applyValues(document, content, table.getSelectedRow(), 0);
        Documents.fireDocumentChanged(this, "Paste", document);
    }

    private Document addOrRemoveRows(Document document, CopiedContent content) {
        return addRequiredRows(removeExcessRows(document, content), content);
    }

    private Document addRequiredRows(Document document, CopiedContent content) {
        return document.withMembers(toList(membersBeforeNew(document), newMembers(content), membersAfterNew(document)));
    }

    private Stream<Member> membersBeforeNew(Document document) {
        return document.members().stream().limit(table.getLastSelectedRow());
    }

    private Stream<Member> membersAfterNew(Document document) {
        return document.members().stream().skip(table.getLastSelectedRow());
    }

    private Stream<Member> newMembers(CopiedContent content) {
        return IntStream.range(0, (content.getRowCount() - table.getSelectedRowCount()))
                .mapToObj(unused -> newMember());
    }

    private Document removeExcessRows(Document document, CopiedContent data) {
        return MemberRemover.remove(document, getRowsToRemove(data));
    }

    private int[] getRowsToRemove(CopiedContent data) {
        return IntStream.of(table.getSelectedRows())
                .sorted()
                .skip(data.getRowCount())
                .toArray();
    }

    private static List<Member> toList(Stream<Member> s1,
                                       Stream<Member> s2,
                                       Stream<Member> s3) {
        return Stream.concat(Stream.concat(s1, s2), s3).toList();
    }
}
