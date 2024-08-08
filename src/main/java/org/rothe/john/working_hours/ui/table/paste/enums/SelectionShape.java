package org.rothe.john.working_hours.ui.table.paste.enums;

import javax.swing.JTable;

public enum SelectionShape {
    NONE,
    SINGLE_CELL,
    PARTIAL_ROW,
    COMPLETE_ROW,
    FRAGMENT,
    MULTIPLE_ROWS,
    ENTIRE_TABLE;

    public boolean isFragmentStyleSelection() {
        return this == SINGLE_CELL
                || this == PARTIAL_ROW
                || this == FRAGMENT;
    }

    public boolean isEntireTable() {
        return this == ENTIRE_TABLE;
    }

    public boolean isCompleteRows() {
        return isEntireTable() || this == COMPLETE_ROW || this == MULTIPLE_ROWS;
    }

    public static SelectionShape of(JTable table) {
        if(table.getSelectedRowCount() == 0) {
            if(table.getRowCount() == 0) {
                return ENTIRE_TABLE;
            }
            return NONE;
        }

        if(table.getSelectedRowCount() == table.getRowCount() && table.getSelectedColumnCount() == table.getColumnCount()) {
            return ENTIRE_TABLE;
        }

        if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == table.getColumnCount()) {
            return COMPLETE_ROW;
        }

        if(table.getSelectedRowCount() > 1 && table.getSelectedColumnCount() == table.getColumnCount()) {
            return MULTIPLE_ROWS;
        }

        if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == 1) {
            return SINGLE_CELL;
        }

        if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() != table.getColumnCount()) {
            return PARTIAL_ROW;
        }

        return FRAGMENT;
    }
}
