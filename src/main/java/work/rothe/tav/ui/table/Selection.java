package work.rothe.tav.ui.table;

import javax.swing.JTable;
import java.util.Arrays;

record Selection(int[] rows, int[] columns) {
    public static Selection of(JTable table) {
        return new Selection(table.getSelectedRows(), table.getSelectedColumns());
    }

    public boolean empty() {
        return rows.length == 0 || columns.length == 0;
    }

    public void apply(JTable table) {
        if (empty()) {
            return;
        }

        table.clearSelection();

        applyRows(table);

        applyColumns(table);
    }

    private void applyRows(JTable table) {
        Arrays.stream(rows)
                .filter(r -> r >= 0 && r < table.getRowCount())
                .forEach(r -> table.addRowSelectionInterval(r, r));
    }

    private void applyColumns(JTable table) {
        Arrays.stream(columns)
                .filter(c -> c >= 0 && c < table.getColumnCount())
                .forEach(c -> table.addColumnSelectionInterval(c, c));
    }
}
