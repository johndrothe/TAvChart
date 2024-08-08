package org.rothe.john.working_hours.ui.table.paste.enums;

import org.rothe.john.working_hours.ui.table.paste.CopiedContent;

public enum ContentShape {
    NONE,
    SINGLE_VALUE,
    PARTIAL_ROW,
    COMPLETE_ROW,
    MULTIPLE_ROWS,
    FRAGMENT;

    public boolean isFragmentStyleData() {
        return this == SINGLE_VALUE
                || this == PARTIAL_ROW
                || this == FRAGMENT;
    }

    public boolean isCompleteRows() {
        return this == COMPLETE_ROW || this == MULTIPLE_ROWS;
    }

    public static ContentShape of(CopiedContent data, int tableColumnCount) {
        if (data.isEmpty()) {
            return ContentShape.NONE;
        }

        if (data.getColumnCount() == tableColumnCount) {
            if (data.getRowCount() == 1) {
                return ContentShape.COMPLETE_ROW;
            }
            return ContentShape.MULTIPLE_ROWS;
        }

        if (data.getRowCount() == 1) {
            if (data.getColumnCount() == 1) {
                return ContentShape.SINGLE_VALUE;
            }
            return ContentShape.PARTIAL_ROW;
        }

        return ContentShape.FRAGMENT;

    }
}
