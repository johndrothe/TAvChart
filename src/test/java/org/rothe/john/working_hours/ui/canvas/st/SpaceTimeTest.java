package org.rothe.john.working_hours.ui.canvas.st;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpaceTimeTest {
    private final Zone UTC = Zone.fromCsv("Z");
    private final Zone NYC = Zone.fromCsv("America/New_York");

    private final CanvasInfo canvasInfo = mock(CanvasInfo.class);
    private final SpaceTime st = SpaceTime.from(canvasInfo);

    SpaceTimeTest() {
        mockCanvasInfo();
    }

    @Test
    void testToCenterBoundaries_NoSplit_UTC() {
        assertEquals(List.of(new Boundaries(1050, 1950)), st.toCenterBoundaries(pair(UTC, 8, 17)));
    }

    @Test
    void testToCenterBoundaries_Split_UTC() {
        val expected = List.of(new Boundaries(2250, 2650), new Boundaries(250, 650));
        val result = st.toCenterBoundaries(pair(UTC, 20, 4));
        assertEquals(expected, result);
    }

    @Test
    void testToCenterBoundaries_NoSplit_NYC() {
        assertEquals(List.of(new Boundaries(1450, 2350)), st.toCenterBoundaries(pair(NYC, 8, 17)));
    }

    @Test
    void testToCenterBoundaries_Split_NYC() {
        val expected = List.of(new Boundaries(2250, 2650), new Boundaries(250, 650));
        val result = st.toCenterBoundaries(pair(NYC, 16, 0));
        assertEquals(expected, result);
    }

    @Test
    void testSplitAroundBorder_NoSplit_UTC() {
        val pair = new TimePair(Time.at(UTC, 8), Time.at(UTC, 17));
        assertEquals(List.of(pair), st.splitAroundBorder(pair));
    }

    @Test
    void testSplitAroundBorder_NoSplit_NYC() {
        val pair = pair(NYC, 8, 17);
        assertEquals(List.of(pair), st.splitAroundBorder(pair));
    }

    @Test
    void testSplitAroundBorder_NYC() {
        val expected = List.of(pair(NYC, 18, 20), pair(NYC, 20, 4));
        val result = st.splitAroundBorder(pair(NYC, 18, 4));
        assertEquals(expected, result);
    }

    @Test
    void testToColumnStart_UTC() {
        assertEquals(200, st.toColumnStart(Time.at(UTC, 0)));
        assertEquals(300, st.toColumnStart(Time.at(UTC, 1)));
        assertEquals(400, st.toColumnStart(Time.at(UTC, 2)));
    }

    @Test
    void testToColumnStart_NYC() {
        assertEquals(200, st.toColumnStart(Time.at(NYC, 20)));
        assertEquals(300, st.toColumnStart(Time.at(NYC, 21)));
        assertEquals(400, st.toColumnStart(Time.at(NYC, 22)));
    }

    @Test
    void testToColumnStart_Int() {
        assertEquals(200, st.toColumnStart(0));
        assertEquals(300, st.toColumnStart(60));
        assertEquals(400, st.toColumnStart(120));
    }

    @Test
    void testToColumnCenter_Int() {
        assertEquals(250, SpaceTime.toColumnCenter(0, 200, 100.0));
        assertEquals(350, SpaceTime.toColumnCenter(60, 200, 100.0));
        assertEquals(450, SpaceTime.toColumnCenter(120, 200, 100.0));
        assertEquals(350, SpaceTime.toColumnCenter(120, 100, 100.0));
    }

    @Test
    void testToColumnCenter_Time_UTC() {
        assertEquals(250, st.toColumnCenter(Time.at(UTC, 0)));
        assertEquals(350, st.toColumnCenter(Time.at(UTC, 1)));
        assertEquals(450, st.toColumnCenter(Time.at(UTC, 2)));
    }

    @Test
    void testToColumnCenter_Time_NYC() {
        assertEquals(250, st.toColumnCenter(Time.at(NYC, 20)));
        assertEquals(350, st.toColumnCenter(Time.at(NYC, 21)));
        assertEquals(450, st.toColumnCenter(Time.at(NYC, 22)));
    }

    @Test
    void testGetRightColumnCenter() {
        // 200 + (25 * 100.0) + (100.0 / 2)
        assertEquals(2650, st.getRightColumnCenter());
    }

    private static TimePair pair(Zone zone, int left, int right) {
        return new TimePair(Time.at(zone, left), Time.at(zone, right));
    }

    private void mockCanvasInfo() {
        when(canvasInfo.getBorderHour()).thenReturn(0);
        when(canvasInfo.getCenterHour()).thenReturn(12);
        when(canvasInfo.getHourColumnWidth()).thenReturn(100.0);
        when(canvasInfo.getRowHeaderWidth()).thenReturn(200);
        when(canvasInfo.getRowFooterWidth()).thenReturn(300);
    }
}