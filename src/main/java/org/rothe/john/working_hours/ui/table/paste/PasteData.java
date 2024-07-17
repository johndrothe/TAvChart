package org.rothe.john.working_hours.ui.table.paste;

import java.util.ArrayList;
import java.util.List;

public class PasteData {
    private static final String LF = "\n";
    private static final String TAB = "\t";

    private final String data;
    private final List<Row> rows;
    private final int columnCount;

    private PasteData(String data) {
        this.data = data;
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

    private static int countColumns(List<Row> rows) {
        return rows.stream().mapToInt(Row::size).min().orElse(0);
    }

    public int getRowCount() {
        return rows.size();
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

    public DataShape shape(int tableColumnCount) {
        if (data.trim().isEmpty()) {
            return DataShape.NONE;
        }

        if (rows.size() == 1) {
            return shapeOf(rows.getFirst(), tableColumnCount);
        }

        //TODO
        return null;
    }

    private DataShape shapeOf(Row row, int tableColumnCount) {
        if (row.isEmpty()) {
            return DataShape.NONE;
        }

        if (row.size() == tableColumnCount) {
            return DataShape.COMPLETE_ROW;
        }

        if (row.size() == 1) {
            return DataShape.SINGLE_VALUE;
        }

        return DataShape.PARTIAL_ROW;
    }

    private static List<Row> toRows(String data) {
        List<Row> rows = new ArrayList<>();
        for (String row : data.split(LF + "+")) {
            rows.add(new Row(row.split(TAB)));
        }
        return rows;
    }

    private record Row(String[] columns) {
        public int size() {
            return columns.length;
        }

        public boolean isEmpty() {
            return columns.length == 0;
        }

        public String get(int index) {
            return columns[index];
        }
    }

}
