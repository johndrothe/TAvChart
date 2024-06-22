package org.rothe.john.working_hours.ui.table;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class ColumnSizing {
    private static final int MINIMUM = 15;
    private static final int MAXIMUM = 500;
    private static final int PADDING = 10;
    private final JTable table;

    private ColumnSizing(JTable table) {
        this.table = table;
    }

    public static void adjust(JTable table) {
        if (isNull(table.getTableHeader())) {
            return;
        }
        new ColumnSizing(table).adjust();
    }

    private void adjust() {
        IntStream.range(0, table.getColumnCount())
                .forEach(column -> setColumnWidth(column, getColumnWidth(column)));
    }

    private int getColumnWidth(int column) {
        return Math.min(MAXIMUM,
                max(getColumnMinimum(column),
                        getMaxRowWidth(column),
                        getColumn(column).getPreferredWidth()));
    }

    private int getMaxRowWidth(final int column) {
        return IntStream.range(0, table.getRowCount())
                .map(row -> getMaxRowWidth(row, column))
                .max()
                .orElse(MINIMUM) + PADDING;
    }

    private int getMaxRowWidth(int row, int column) {
        return getCellRenderer(row, column).getPreferredSize().width;
    }

    private int getColumnMinimum(final int column) {
        if (column == Columns.ZONE.ordinal()) {
            return 300;
        }
        return MINIMUM;
    }

    private Component getCellRenderer(int row, int column) {
        return table.prepareRenderer(table.getCellRenderer(row, column), row, column);
    }

    private void setColumnWidth(int columnIndex, int width) {
        setColumnWidth(getColumn(columnIndex), width);
    }

    private void setColumnWidth(TableColumn column, int width) {
        column.setWidth(width);
        column.setPreferredWidth(width);
    }

    private TableColumn getColumn(int columnIndex) {
        return table.getColumnModel().getColumn(columnIndex);
    }

    private int max(int v1, int v2, int v3) {
        return Math.max(v1, Math.max(v2, v3));
    }
}

