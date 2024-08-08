package org.rothe.john.working_hours.ui.table.paste.enums;

public enum ReplaceBehavior {
    /**
     * Replace the entire table.
     */
    ENTIRE_TABLE,
    /**
     * Replace the selected rows with the rows in the data.  Add/Remove as necessary.
     */
    ROWS,
    /**
     * Data > Selection: Start pasting at the first selected cell.
     * Data <= Selection: Paste one or more complete copies of the data into the selected cells
     */
    VALUES;

    public static ReplaceBehavior of(SelectionShape selectionShape, ContentShape contentShape) {
        if (selectionShape.isEntireTable() && contentShape.isCompleteRows()) {
            return ENTIRE_TABLE;
        } else if (contentShape.isCompleteRows() && selectionShape.isCompleteRows()) {
            return ROWS;
        } else if (contentShape.isFragmentStyleData() || selectionShape.isFragmentStyleSelection()) {
            return VALUES;
        } else {
            throw new UnsupportedOperationException("Unhandled paste of " + contentShape + " into " + selectionShape);
        }
    }
}
