package work.rothe.tav.ui.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Component;

public class TextCellEditor extends DefaultCellEditor {
    public TextCellEditor() {
        super(new JTextField());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        if(c instanceof JTextField field) {
            field.selectAll();
            field.requestFocusInWindow();
        }
        return c;
    }
}
