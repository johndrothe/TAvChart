package org.rothe.john.working_hours.ui.table.paste;

import java.util.Arrays;
import java.util.List;

import static java.util.function.Predicate.not;

public class PasteData {
    private static final String LF = "\n";
    private static final String TAB = "\t";

    private final List<Row> rows;
    private final int columnCount;

    private PasteData(String data) {
        this.rows = toRows(data);
        this.columnCount = countColumns(rows);
    }

    public static PasteData of(Object data) {
        return new PasteData((String) data);
    }

    public static PasteData of(String data) {
        return new PasteData(data);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rows.size();
    }

    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    public String getValueAt(int row, int column) {
        if (row < 0 || row >= getRowCount()) {
            throw new IndexOutOfBoundsException("Row out of bounds: " + row);
        }
        if (column < 0 || column >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Column out of bounds: " + column);
        }
        return rows.get(row).get(column);
    }

    private static int countColumns(List<Row> rows) {
        return rows.stream().mapToInt(Row::size).min().orElse(0);
    }

    private static List<Row> toRows(String data) {
        return Arrays.stream(data.split(LF + "+"))
                .filter(not(String::isEmpty))
                .map(line -> line.split(TAB))
                .map(Row::new)
                .filter(not(Row::isEmpty))
                .toList();
    }

    private record Row(String[] columns) {
        public int size() {
            return columns.length;
        }

        public boolean isEmpty() {
            return columns.length == 0 || (columns.length == 1 && columns[0].isEmpty());
        }

        public String get(int index) {
            return columns[index];
        }
    }
}
