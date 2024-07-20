package org.rothe.john.working_hours.ui.table.paste;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rothe.john.working_hours.ui.table.paste.ContentShape.COMPLETE_ROW;
import static org.rothe.john.working_hours.ui.table.paste.ContentShape.FRAGMENT;
import static org.rothe.john.working_hours.ui.table.paste.ContentShape.MULTIPLE_ROWS;
import static org.rothe.john.working_hours.ui.table.paste.ContentShape.NONE;
import static org.rothe.john.working_hours.ui.table.paste.ContentShape.PARTIAL_ROW;
import static org.rothe.john.working_hours.ui.table.paste.ContentShape.SINGLE_VALUE;

class ContentShapeTest {

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

    private static ContentShape shape(String data, int tableColumnCount) {
        return CopiedContent.of(data, tableColumnCount).shape();
    }
}