package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.util.Images;

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
