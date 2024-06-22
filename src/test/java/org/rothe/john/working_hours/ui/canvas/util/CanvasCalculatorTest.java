package org.rothe.john.working_hours.ui.canvas.util;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.TimePair;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.canvas.rows.CanvasRow;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CanvasCalculatorTest {
    private final Zone UTC = Zone.fromCsv("Z");
    private final Zone NYC = Zone.fromCsv("America/New_York");

    private final RowList rowList = mock(RowList.class);
    private final CanvasCalculator calculator = new CanvasCalculator(rowList);

    CanvasCalculatorTest() {
        mockRowList();
        calculator.update(null);
    }

    @Test
    void testToCenterBoundaries_NoSplit_UTC() {
        assertEquals(List.of(new Boundaries(1050, 1950)), calculator.toCenterBoundaries(pair(UTC, 8, 17)));
    }

    @Test
    void testToCenterBoundaries_Split_UTC() {
        val expected = List.of(new Boundaries(2250, 2650), new Boundaries(250, 650));
        val result = calculator.toCenterBoundaries(pair(UTC, 20, 4));
        assertEquals(expected, result);
    }

    @Test
    void testToCenterBoundaries_NoSplit_NYC() {
        assertEquals(List.of(new Boundaries(1450, 2350)), calculator.toCenterBoundaries(pair(NYC, 8, 17)));
    }

    @Test
    void testToCenterBoundaries_Split_NYC() {
        val expected = List.of(new Boundaries(2250, 2650), new Boundaries(250, 650));
        val result = calculator.toCenterBoundaries(pair(NYC, 16, 0));
        assertEquals(expected, result);
    }

    @Test
    void testSplitAroundBorder_NoSplit_UTC() {
        val pair = new TimePair(Time.at(UTC, 8), Time.at(UTC, 17));
        assertEquals(List.of(pair), calculator.splitAroundBorder(pair));
    }

    @Test
    void testSplitAroundBorder_NoSplit_NYC() {
        val pair = pair(NYC, 8, 17);
        assertEquals(List.of(pair), calculator.splitAroundBorder(pair));
    }

    @Test
    void testSplitAroundBorder_NYC() {
        val expected = List.of(pair(NYC, 18, 20), pair(NYC, 20, 4));
        val result = calculator.splitAroundBorder(pair(NYC, 18, 4));
        assertEquals(expected, result);
    }

    @Test
    void testToColumnStart_UTC() {
        assertEquals(200, calculator.toColumnStart(Time.at(UTC, 0)));
        assertEquals(300, calculator.toColumnStart(Time.at(UTC, 1)));
        assertEquals(400, calculator.toColumnStart(Time.at(UTC, 2)));
    }

    @Test
    void testToColumnStart_NYC() {
        assertEquals(200, calculator.toColumnStart(Time.at(NYC, 20)));
        assertEquals(300, calculator.toColumnStart(Time.at(NYC, 21)));
        assertEquals(400, calculator.toColumnStart(Time.at(NYC, 22)));
    }

    @Test
    void testToColumnStart_Int() {
        assertEquals(200, calculator.toColumnStart(0));
        assertEquals(300, calculator.toColumnStart(60));
        assertEquals(400, calculator.toColumnStart(120));
    }

    @Test
    void testToColumnCenter_Int() {
        assertEquals(250, CanvasCalculator.toColumnCenter(0, 200, 100.0));
        assertEquals(350, CanvasCalculator.toColumnCenter(60, 200, 100.0));
        assertEquals(450, CanvasCalculator.toColumnCenter(120, 200, 100.0));
        assertEquals(350, CanvasCalculator.toColumnCenter(120, 100, 100.0));
    }

    @Test
    void testToColumnCenter_Time_UTC() {
        assertEquals(250, calculator.toColumnCenter(Time.at(UTC, 0)));
        assertEquals(350, calculator.toColumnCenter(Time.at(UTC, 1)));
        assertEquals(450, calculator.toColumnCenter(Time.at(UTC, 2)));
    }

    @Test
    void testToColumnCenter_Time_NYC() {
        assertEquals(250, calculator.toColumnCenter(Time.at(NYC, 20)));
        assertEquals(350, calculator.toColumnCenter(Time.at(NYC, 21)));
        assertEquals(450, calculator.toColumnCenter(Time.at(NYC, 22)));
    }

    @Test
    void testGetRightColumnCenter() {
        // 200 + (25 * 100.0) + (100.0 / 2)
        assertEquals(2650, calculator.getRightColumnCenter());
    }

    private static TimePair pair(Zone zone, int left, int right) {
        return new TimePair(Time.at(zone, left), Time.at(zone, right));
    }

    private void mockRowList() {
        CanvasRow row = mock(CanvasRow.class);
        when(row.getWidth()).thenReturn(3000);

        when(rowList.getFirst()).thenReturn(row);

        //when(rowList.getHourColumnWidth()).thenReturn(100.0);
        when(rowList.getColumnHeaderWidth(any())).thenReturn(200);
        when(rowList.getColumnFooterWidth(any())).thenReturn(300);
    }
}