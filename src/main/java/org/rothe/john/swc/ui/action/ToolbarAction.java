package org.rothe.john.swc.ui.action;

import javax.swing.AbstractAction;
import javax.swing.Icon;

public abstract class ToolbarAction extends AbstractAction {
    protected ToolbarAction(String name, Icon icon) {
        super(name, icon);
        putValue(SHORT_DESCRIPTION, name);
    }
}
