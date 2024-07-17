package org.rothe.john.working_hours.ui.table.paste;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rothe.john.working_hours.ui.table.paste.DataShape.COMPLETE_ROW;
import static org.rothe.john.working_hours.ui.table.paste.DataShape.FRAGMENT;
import static org.rothe.john.working_hours.ui.table.paste.DataShape.MULTIPLE_ROWS;
import static org.rothe.john.working_hours.ui.table.paste.DataShape.NONE;
import static org.rothe.john.working_hours.ui.table.paste.DataShape.PARTIAL_ROW;
import static org.rothe.john.working_hours.ui.table.paste.DataShape.SINGLE_VALUE;

class DataShapeTest {

    @Test
    void testShapeNone() {
        assertEquals(NONE, shape("", 2));
    }

    @Test
    void testShapeSingle() {
        assertEquals(SINGLE_VALUE, shape("A", 2));
    }

    @Test
    void testShapeSingleCompleteRow() {
        assertEquals(COMPLETE_ROW, shape("A", 1));
    }

    @Test
    void testShapeDoubleCompleteRow() {
        assertEquals(COMPLETE_ROW, shape("A\tB", 2));
    }

    @Test
    void testShapePartialRow() {
        assertEquals(PARTIAL_ROW, shape("A\tB", 3));
    }

    @Test
    void testShapeMultipleRows() {
        assertEquals(MULTIPLE_ROWS, shape("A\tB\nC\tD", 2));
    }
    @Test
    void testShapeFragment() {
        assertEquals(FRAGMENT, shape("A\tB\nC\tD", 3));
    }

    private static DataShape shape(String data, int tableColumnCount) {
        return DataShape.of(PasteData.of(data), tableColumnCount);
    }
}