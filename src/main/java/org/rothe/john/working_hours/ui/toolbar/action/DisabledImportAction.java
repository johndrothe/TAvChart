package org.rothe.john.working_hours.ui.toolbar.action;

import org.rothe.john.working_hours.ui.util.Images;

import java.awt.event.ActionEvent;

public class DisabledImportAction extends ToolbarAction {
    public DisabledImportAction() {
        super("Import", Images.load("load.png"));
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
