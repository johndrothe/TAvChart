package work.rothe.tav.ui.table.paste;

import lombok.extern.slf4j.Slf4j;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.enums.ReplaceBehavior;
import work.rothe.tav.ui.table.paste.enums.SelectionShape;
import work.rothe.tav.ui.table.paste.operations.EntireTableOperation;
import work.rothe.tav.ui.table.paste.operations.RowOperation;
import work.rothe.tav.ui.table.paste.operations.ValueOperation;
import work.rothe.tav.util.Pair;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

@Slf4j
public class Paster {
    private final MembersTable table;

    private Paster(MembersTable table) {
        this.table = table;
    }

    public static void paste(MembersTable table) {
        new Paster(table).paste();
    }

    public static boolean canPaste(MembersTable table) {
        return (noSelection(table) || isReplaceableSelection(table)) && isClipboardTextAvailable();
    }

    private void paste() {
        try {
            paste(CopiedContent.of(getTextFromClipboard(), table.getColumnCount()));
        } catch (IOException | UnsupportedFlavorException ex) {
            log.error("Failed to paste from clipboard: {}", ex.getMessage(), ex);
        }
    }

    private void paste(CopiedContent content) {
        if (content.isEmpty() || !canPaste(table)) {
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

    private static boolean noSelection(MembersTable table) {
        return table.getSelectedRowCount() == 0;
    }

    private static boolean hasSelection(MembersTable table) {
        return table.getSelectedRowCount() > 0 && table.getSelectedColumnCount() > 0;
    }

    private static boolean isReplaceableSelection(MembersTable table) {
        return hasSelection(table) && isContiguousSelection(table);
    }

    private static boolean isContiguousSelection(MembersTable table) {
        return Pair.stream(table.getSelectedRowList())
                .noneMatch(p -> Math.abs(p.left() - p.right()) != 1);
    }

    private static String getTextFromClipboard()
            throws IOException, UnsupportedFlavorException {
        if (isClipboardTextAvailable()) {
            return (String) getClipboard().getData(stringFlavor);
        }
        return "";
    }

    private static boolean isClipboardTextAvailable() {
        return getClipboard().isDataFlavorAvailable(stringFlavor);
    }

    private static Clipboard getClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}
