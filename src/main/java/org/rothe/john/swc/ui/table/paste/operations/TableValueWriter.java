package org.rothe.john.swc.ui.table.paste.operations;

import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.table.MembersTableModel;

class TableValueWriter {
    private final PasteTableModel model = new PasteTableModel();
    private Document document;
    private final int baseRow;
    private final int baseColumn;

    private int currentRow;
    private int currentColumn;

    public TableValueWriter(Document document, int baseRow, int baseColumn) {
        this.document = document;
        this.currentRow = this.baseRow = baseRow;
        this.currentColumn = this.baseColumn = baseColumn;
    }

    public void setNext(String value) {
        model.setDocument(document);
        try {
            document = model.setValue(value, currentRow, currentColumn++);
        } catch (Exception e) {
            // Quietly allow (but ignore) inappropriate values
            e.printStackTrace();
        }
    }

    public void nextRow() {
        ++currentRow;
        currentColumn = baseColumn;
    }

    public Document document() {
        return document;
    }

    private static class PasteTableModel extends MembersTableModel {
        @Override
        public Document setValue(Object aValue, int rowIndex, int columnIndex) {
            return super.setValue(aValue, rowIndex, columnIndex);
        }
    }
}
