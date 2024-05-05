package org.rothe.john.working_hours.ui.table;

import org.rothe.john.working_hours.ui.action.CopyAction;
import org.rothe.john.working_hours.ui.action.ExportCsvAction;
import org.rothe.john.working_hours.ui.action.ImportCsvAction;
import org.rothe.john.working_hours.ui.action.MoveDownAction;
import org.rothe.john.working_hours.ui.action.MoveUpAction;
import org.rothe.john.working_hours.ui.action.PasteAction;

import javax.swing.JToolBar;

public class Toolbar extends JToolBar {
    public Toolbar() {
        super();
        initialize();
    }

    private void initialize() {
        setFloatable(false);
        add(new ImportCsvAction(this));
        add(new ExportCsvAction(this));
        addSeparator();
        add(new CopyAction(this));
        add(new PasteAction(this));
        addSeparator();
        add(new MoveUpAction(this));
        add(new MoveDownAction(this));
    }
}
