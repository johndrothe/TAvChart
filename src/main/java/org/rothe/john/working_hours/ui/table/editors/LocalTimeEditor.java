package org.rothe.john.working_hours.ui.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class LocalTimeEditor extends DefaultCellEditor {
    public LocalTimeEditor() {
        super(new JTextField());
    }
}
