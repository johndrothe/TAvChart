package org.rothe.john.swc.ui.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class LocalTimeEditor extends DefaultCellEditor {
    public LocalTimeEditor() {
        super(new JTextField());
    }
}
