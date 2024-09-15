package work.rothe.tav.ui.table.paste.operations;

import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.CopiedContent;

public class ValueOperation extends AbstractPasteOperation {
    private ValueOperation(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new ValueOperation(content, table).paste();
    }

    private void paste() {
        if(isSingleCellSelected() || isDataLargerThanSelection()) {
            // Data > Selection: Start pasting at the first selected cell.
            Documents.fireDocumentChanged(this, "Paste", pasteOneCopy());
        } else {
            // Data <= Selection: Paste one or more complete copies of the data into the selected cells
            Documents.fireDocumentChanged(this, "Paste", pasteMultipleCopies());
        }
    }

    private Document pasteMultipleCopies() {
        Document document = table.getDocument();
        for(int vertical = 0; vertical < getVerticalCopies(); ++vertical) {
            for(int horizontal = 0; horizontal < getHorizontalCopies(); ++horizontal) {

                int targetRow = table.getSelectedRow() + (horizontal * content.getRowCount());
                int targetColumn = table.getSelectedColumn() + (vertical * content.getColumnCount());
                document = applyValues(document, content, targetRow, targetColumn);
            }
        }
        return document;
    }

    private Document pasteOneCopy() {
        return applyValues(table.getDocument(), content, table.getSelectedRow(), table.getSelectedColumn());
    }

    private boolean isSingleCellSelected() {
        return table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == 1;
    }

    private boolean isDataLargerThanSelection() {
        return content.getRowCount() >= table.getSelectedRowCount()
                && content.getColumnCount() >= table.getSelectedColumnCount();
    }

    private int getHorizontalCopies() {
        return table.getSelectedRowCount() / content.getRowCount();
    }

    private int getVerticalCopies() {
        return table.getSelectedColumnCount() / content.getColumnCount();
    }
}
