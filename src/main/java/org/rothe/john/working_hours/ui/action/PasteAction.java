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

//        switch (Selection.of(table)) {
//
//        }
//
//        PasteData data = PasteData.of(clipboard.getData(stringFlavor));
//        switch(data.shape(table.getColumnCount())) {
//
//        }

    }



}
