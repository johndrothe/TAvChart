package work.rothe.tav.ui.canvas.util;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.rothe.tav.model.Time;
import work.rothe.tav.model.TimePair;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.canvas.rows.CanvasRow;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CanvasCalculatorTest {
    private final Zone UTC = Zone.fromCsv("Z");
    private final Zone NYC = Zone.fromCsv("America/New_York");

    private final Canvas canvas = mock(Canvas.class);
    private final RowList rowList = mock(RowList.class);
    private final CanvasCalculator calculator = new CanvasCalculator(canvas, rowList);

    CanvasCalculatorTest() {
        mockRowList();
        calculator.update(null);
    }

    @BeforeEach
    void setUp() {
        reset(canvas);
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
        assertEquals(2600, calculator.toColumnStart(Time.MINUTES_IN_A_DAY));
    }


    @Test
    void testToColumnStart_Int_BorderHour_1() {
        when(canvas.getBorderHour()).thenReturn(1);
        assertEquals(200, calculator.toColumnStart(60));
        assertEquals(300, calculator.toColumnStart(120));
        assertEquals(400, calculator.toColumnStart(180));

        assertEquals(2500, calculator.toColumnStart(0));
    }

    @Test
    void testToColumnCenter_Int() {
        assertEquals(250, calculator.toColumnCenter(0, 200, 100.0));
        assertEquals(350, calculator.toColumnCenter(60, 200, 100.0));
        assertEquals(450, calculator.toColumnCenter(120, 200, 100.0));
        assertEquals(350, calculator.toColumnCenter(120, 100, 100.0));
        assertEquals(2550, calculator.toColumnCenter(24*60, 100, 100.0));
    }

    @Test
    void testToColumnCenter_Int_BorderHour_1() {
        when(canvas.getBorderHour()).thenReturn(1);

        assertEquals(250, calculator.toColumnCenter(60, 200, 100.0));
        assertEquals(350, calculator.toColumnCenter(120, 200, 100.0));
        assertEquals(250, calculator.toColumnCenter(120, 100, 100.0));

        assertEquals(2550, calculator.toColumnCenter(0, 200, 100.0));
    }

    @Test
    void testToColumnCenter_Int_BorderHour_4() {
        when(canvas.getBorderHour()).thenReturn(4);
        assertEquals(250, calculator.toColumnCenter(240, 200, 100.0));
        assertEquals(350, calculator.toColumnCenter(300, 200, 100.0));
        assertEquals(250, calculator.toColumnCenter(300, 100, 100.0));
    }

    @Test
    void testToColumnCenter_Int_BorderHour_10() {
        when(canvas.getBorderHour()).thenReturn(10);
        assertEquals(250, calculator.toColumnCenter(600, 200, 100.0));
        assertEquals(350, calculator.toColumnCenter(660, 200, 100.0));
        assertEquals(250, calculator.toColumnCenter(660, 100, 100.0));
    }

    @Test
    void testToColumnCenter_Int_BorderHour_20() {
        when(canvas.getBorderHour()).thenReturn(20);
        assertEquals(250, calculator.toColumnCenter(1200, 200, 100.0));
        assertEquals(350, calculator.toColumnCenter(1260, 200, 100.0));
        assertEquals(250, calculator.toColumnCenter(1260, 100, 100.0));
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