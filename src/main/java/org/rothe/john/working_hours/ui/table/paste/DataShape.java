package org.rothe.john.working_hours.ui.table.paste;

public enum DataShape {
    NONE,
    SINGLE_VALUE,
    PARTIAL_ROW,
    COMPLETE_ROW,
    MULTIPLE_ROWS,
    FRAGMENT;

    public static DataShape of(PasteData data, int tableColumnCount) {
        if (data.isEmpty()) {
            return DataShape.NONE;
        }

        if (data.getColumnCount() == tableColumnCount) {
            if (data.getRowCount() == 1) {
                return DataShape.COMPLETE_ROW;
            }
            return DataShape.MULTIPLE_ROWS;
        }

        if (data.getRowCount() == 1) {
            if (data.getColumnCount() == 1) {
                return DataShape.SINGLE_VALUE;
            }
            return DataShape.PARTIAL_ROW;
        }

        return DataShape.FRAGMENT;

    }
}
