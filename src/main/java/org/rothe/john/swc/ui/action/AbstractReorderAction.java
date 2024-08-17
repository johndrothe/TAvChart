package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.table.MembersTable;

import javax.swing.*;
import java.util.Arrays;

abstract class AbstractReorderAction extends ToolbarAction {
    private final MembersTable table;

    public AbstractReorderAction(MembersTable table, String name, ImageIcon icon) {
        super(name, icon);
        this.table = table;
    }

    protected Document document() {
        return table.getDocument();
    }

    protected MembersTable table() {
        return table;
    }

    protected void fireDocumentChanged(String change, Document newDocument) {
        Documents.fireDocumentChanged(this, change, newDocument);
    }

    protected void selectRowsLater(int[] rows) {
        SwingUtilities.invokeLater(() -> selectRows(rows));
    }

    private void selectRows(int[] rows) {
        table.clearSelection();

        table.addColumnSelectionInterval(0, table.getColumnCount() - 1);
        for (int row : rows) {
            table.addRowSelectionInterval(row, row);
        }
    }

    protected static int[] adjust(int[] array, int value) {
        int[] a = Arrays.copyOf(array, array.length);
        for (int i = 0; i < a.length; ++i) {
            a[i] += value;
        }
        return a;
    }
}
