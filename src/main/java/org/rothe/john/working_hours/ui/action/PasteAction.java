package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.util.Images;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

public class PasteAction extends ToolbarAction {
    private final MembersTable table;

    public PasteAction(MembersTable table) {
        super("Paste", Images.load("paste.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(stringFlavor)) {
            try {
                paste(clipboard);
            } catch (IOException | UnsupportedFlavorException ex) {
                //TODO: Undo the paste
                System.err.printf("Error: Failed to paste from clibpard: %s%n", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void paste(Clipboard clipboard)
            throws IOException, UnsupportedFlavorException {
        String data = (String) clipboard.getData(stringFlavor);

        switch (getSelectionType()) {

        }

        if (isSingleValue(data)) {

        }
    }

    private Selection getSelectionType() {
        if(table.getSelectedRowCount() == 0) {
            if(table.getRowCount() == 0) {
                return Selection.ENTIRE_TABLE;
            }
            return Selection.NONE;
        } else if(table.getSelectedRowCount() == table.getRowCount() && table.getSelectedColumnCount() == table.getColumnCount()) {
            return Selection.ENTIRE_TABLE;
        } else if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == table.getColumnCount()) {
            return Selection.COMPLETE_ROW;
        } else if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == 1) {
            return Selection.SINGLE_CELL;
        }
        
    }

    private boolean isSingleValue(String data) {
        return !(data.contains("\t") || data.contains("\n"));
    }

    private enum DataShape {
        SINGLE_VALUE,
        PARTIAL_ROW,
        COMPLETE_ROW,
        MULTIPLE_ROWS,
        FRAGMENT;
    }

    private enum Selection {
        NONE,
        SINGLE_CELL,
        PARTIAL_ROW,
        COMPLETE_ROW,
        FRAGMENT,
        ENTIRE_TABLE;
    }
}
