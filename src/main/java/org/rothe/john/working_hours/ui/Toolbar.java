package org.rothe.john.working_hours.ui;

import org.rothe.john.working_hours.ui.action.ExportCsvAction;
import org.rothe.john.working_hours.ui.action.ImportCsvAction;
import org.rothe.john.working_hours.ui.canvas.Canvas;

import javax.swing.JToolBar;

public class Toolbar extends JToolBar {
    private final Canvas canvas;

    public Toolbar(Canvas canvas) {
        super();
        this.canvas = canvas;
        initialize();
    }

    private void initialize() {
        setFloatable(false);
        add(new ImportCsvAction(this, canvas));
        add(new ExportCsvAction(this, canvas));
    }
}
