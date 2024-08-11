package org.rothe.john.swc.ui.table.paste;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CopiedContentTest {
    private static final int THREE_COLUMNS = 3;

    @Test
    void testEmpty() {
        assertTrue(contentThree("").isEmpty());
        assertTrue(contentThree("\n").isEmpty());
    }

    @Test
    void testOneRow() {
        assertEquals(1, contentThree("X").getRowCount());
    }

    @Test
    void testOneColumn() {
        assertEquals(1, contentThree("X").getColumnCount());
    }

    @Test
    void testTwoRows() {
        assertEquals(2, contentThree("X\nY").getRowCount());
    }

    @Test
    void testTwoColumns() {
        assertEquals(2, contentThree("X\tY").getColumnCount());
    }

    @Test
    void testTwoRowsAndThreeColumns() {
        val actual = contentThree("A\tB\tC\nD\tE\tF");
        assertEquals(2, actual.getRowCount());
        assertEquals(3, actual.getColumnCount());
    }

    @Test
    void testMismatchedColumns() {
        assertEquals(3, contentThree("A\tB\tC\tG\nD\tE\tF").getColumnCount());
        assertEquals(3, contentThree("A\tB\tC\nD\tE\tF\tG").getColumnCount());
    }

    @Test
    void testValueAt() {
        val actual = contentThree("A\tB\tC\nD\tE\tF");
        assertEquals("A", actual.getValueAt(0, 0));
        assertEquals("B", actual.getValueAt(0, 1));
        assertEquals("C", actual.getValueAt(0, 2));
        assertEquals("D", actual.getValueAt(1, 0));
        assertEquals("E", actual.getValueAt(1, 1));
        assertEquals("F", actual.getValueAt(1, 2));
    }

    @Test
    void testGetValueAtOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> contentThree("A\tB").getValueAt(3, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> contentThree("A\tB").getValueAt(0, 3));
    }

    private static CopiedContent contentThree(String content) {
        return CopiedContent.of(content, THREE_COLUMNS);
    }
}