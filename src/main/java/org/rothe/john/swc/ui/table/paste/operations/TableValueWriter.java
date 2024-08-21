package org.rothe.john.swc.ui.table.paste.operations;

import lombok.extern.slf4j.Slf4j;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.table.MembersTableModel;

@Slf4j
class TableValueWriter {
    private final PasteTableModel model = new PasteTableModel();
    private Document document;
    private final int baseColumn;

    private int currentRow;
    private int currentColumn;

    public TableValueWriter(Document document, int baseRow, int baseColumn) {
        this.document = document;
        this.currentRow = baseRow;
        this.currentColumn = this.baseColumn = baseColumn;
    }

    public void setNext(String value) {
        model.setDocument(document);
        try {
            document = model.setValue(value, currentRow, currentColumn++);
        } catch (Exception e) {
            // Quietly allow (but ignore) inappropriate values
            log.debug("Ignoring invalid value '{}' during paste to [{}, {}].",
                    value, currentRow, currentColumn, e);
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
