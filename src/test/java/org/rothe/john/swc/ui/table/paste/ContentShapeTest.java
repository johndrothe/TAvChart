package org.rothe.john.swc.ui.table.paste;

import org.junit.jupiter.api.Test;
import org.rothe.john.swc.ui.table.paste.enums.ContentShape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rothe.john.swc.ui.table.paste.enums.ContentShape.COMPLETE_ROW;
import static org.rothe.john.swc.ui.table.paste.enums.ContentShape.FRAGMENT;
import static org.rothe.john.swc.ui.table.paste.enums.ContentShape.MULTIPLE_ROWS;
import static org.rothe.john.swc.ui.table.paste.enums.ContentShape.NONE;
import static org.rothe.john.swc.ui.table.paste.enums.ContentShape.PARTIAL_ROW;
import static org.rothe.john.swc.ui.table.paste.enums.ContentShape.SINGLE_VALUE;

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