package org.rothe.john.working_hours.ui;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;
import java.awt.event.ActionEvent;

public class Toolbar extends JToolBar {
    public Toolbar() {
        super();
        initialize();
    }

    private void initialize() {
        setFloatable(false);
        add(new AbstractAction("Import") {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        add(new AbstractAction("Export") {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }
}
