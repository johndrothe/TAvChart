package org.rothe.john.working_hours.ui;

import org.rothe.john.working_hours.ui.canvas.Canvas;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;
import java.awt.event.ActionEvent;

public class Toolbar extends JToolBar {
    private final Canvas canvas;

    public Toolbar(Canvas canvas) {
        super();
        this.canvas = canvas;
        initialize();
    }

    private void initialize() {
        setFloatable(false);
        add(new AbstractAction("Import") {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        add(new ExportCsvAction(this, canvas));
    }
}
