package work.rothe.tav.ui.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class LocalTimeEditor extends DefaultCellEditor {
    public LocalTimeEditor() {
        super(new JTextField());
    }
}
