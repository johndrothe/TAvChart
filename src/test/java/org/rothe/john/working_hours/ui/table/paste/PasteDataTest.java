package org.rothe.john.working_hours.ui.table.paste;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasteDataTest {

    @Test
    void testEmpty() {
        assertTrue(PasteData.of("").isEmpty());
        assertTrue(PasteData.of("\n").isEmpty());
    }

    @Test
    void testOneRow() {
        assertEquals(1, PasteData.of("X").getRowCount());
    }

    @Test
    void testOneColumn() {
        assertEquals(1, PasteData.of("X").getColumnCount());
    }

    @Test
    void testTwoRows() {
        assertEquals(2, PasteData.of("X\nY").getRowCount());
    }

    @Test
    void testTwoColumns() {
        assertEquals(2, PasteData.of("X\tY").getColumnCount());
    }

    @Test
    void testTwoRowsAndThreeColumns() {
        val actual = PasteData.of("A\tB\tC\nD\tE\tF");
        assertEquals(2, actual.getRowCount());
        assertEquals(3, actual.getColumnCount());
    }

    @Test
    void testMismatchedColumns() {
        assertEquals(3, PasteData.of("A\tB\tC\tG\nD\tE\tF").getColumnCount());
        assertEquals(3, PasteData.of("A\tB\tC\nD\tE\tF\tG").getColumnCount());
    }

    @Test
    void testValueAt() {
        val actual = PasteData.of("A\tB\tC\nD\tE\tF");
        assertEquals("A", actual.getValueAt(0, 0));
        assertEquals("B", actual.getValueAt(0, 1));
        assertEquals("C", actual.getValueAt(0, 2));
        assertEquals("D", actual.getValueAt(1, 0));
        assertEquals("E", actual.getValueAt(1, 1));
        assertEquals("F", actual.getValueAt(1, 2));
    }

    @Test
    void testGetValueAtOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> PasteData.of("A\tB").getValueAt(3, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> PasteData.of("A\tB").getValueAt(0, 3));
    }
}