package org.rothe.john.working_hours.ui.toolbar;

import lombok.val;
import org.rothe.john.working_hours.ui.toolbar.action.*;

import javax.swing.JToolBar;

public class Toolbar extends JToolBar {
    public Toolbar() {
        super();
        setFloatable(false);
    }

    public void displayChanged(final DisplayChangeEvent event) {
        removeAll();
        createButtons(event);
        revalidate();
        repaint();
    }

    private void createButtons(DisplayChangeEvent event) {
        val table = event.table().orElse(null);
        val canvas = event.canvas().orElse(null);

        add(new ImportCsvAction(table));
        add(new ExportCsvAction(table));

        addSeparator();
        add(new CopyAction(table));
        add(new PasteAction(table));

        //TODO: undo manager
        addSeparator();
        add(new UndoAction(this));
        add(new RedoAction(this));

        if (event.isTableDisplayed()) {
            addSeparator();
            add(new MoveUpAction(table));
            add(new MoveDownAction(table));
        }

        if (event.isCanvasDisplayed()) {
            addSeparator();
            add(new ExportImageAction(canvas));
        }
    }

}
