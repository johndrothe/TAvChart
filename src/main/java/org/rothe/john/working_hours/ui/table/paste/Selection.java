package org.rothe.john.working_hours.ui.table.paste;

import javax.swing.JTable;

public enum Selection {
    NONE,
    SINGLE_CELL,
    PARTIAL_ROW,
    COMPLETE_ROW,
    FRAGMENT,
    ENTIRE_TABLE;

    public static Selection of(JTable table) {
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

        if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == 1) {
            return SINGLE_CELL;
        }

        if(table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() != table.getColumnCount()) {
            return PARTIAL_ROW;
        }

        return FRAGMENT;
    }
}
