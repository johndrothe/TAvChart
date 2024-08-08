package org.rothe.john.working_hours.ui.table.paste;

import lombok.val;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.table.paste.enums.ReplaceBehavior;
import org.rothe.john.working_hours.ui.table.paste.enums.SelectionShape;
import org.rothe.john.working_hours.ui.table.paste.operations.EntireTableOperation;
import org.rothe.john.working_hours.ui.table.paste.operations.RowOperation;
import org.rothe.john.working_hours.ui.table.paste.operations.ValueOperation;
import org.rothe.john.working_hours.util.Pair;

import java.awt.Toolkit;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

public class Paster {
    private final MembersTable table;

    private Paster(MembersTable table) {
        this.table = table;
    }

    public static void paste(MembersTable table) {
        new Paster(table).paste();
    }

    public static boolean canPaste(MembersTable table) {
        try {
            return table.getSelectedRowCount() > 0
                    && table.getSelectedColumnCount() > 0
                    && isContiguousSelection(table)
                    && !getTextFromClipboard().isEmpty();
        } catch (IOException | UnsupportedFlavorException e) {
            return false;
        }
    }

    private void paste() {
        try {
            paste(CopiedContent.of(getTextFromClipboard(), table.getColumnCount()));
        } catch (IOException | UnsupportedFlavorException ex) {
            System.err.printf("Error: Failed to paste from clipboard: %s%n", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void paste(CopiedContent content) {
        if (content.isEmpty() || table.getSelectedRowCount() == 0) {
            return;
        }

        switch (ReplaceBehavior.of(selection(), content.shape())) {
            case ENTIRE_TABLE -> EntireTableOperation.paste(content, table);
            case ROWS -> RowOperation.paste(content, table);
            case VALUES -> ValueOperation.paste(content, table);
        }
    }

    private SelectionShape selection() {
        return SelectionShape.of(table);
    }

    private static boolean isContiguousSelection(MembersTable table) {
        return Pair.stream(table.getSelectedRowList())
                .noneMatch(p -> Math.abs(p.left() - p.right()) != 1);
    }

    private static String getTextFromClipboard()
            throws IOException, UnsupportedFlavorException {
        val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(stringFlavor)) {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(stringFlavor);
        }
        return "";
    }
}
