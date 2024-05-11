package org.rothe.john.working_hours.ui.canvas.st;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.Zone;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimePairTest {
    private final Zone UTC = Zone.fromCsv("Z");
    private final Zone NYC = Zone.fromCsv("America/New_York");
    private final Zone CHI = Zone.fromCsv("America/Chicago");
    private final Zone LAX = Zone.fromCsv("America/Los_Angeles");
    private final Zone PL = Zone.fromCsv("Poland");


    @Test
    void testIsSplitBetween() {
        assertTrue(pair(NYC, 18, 4).isSplitBetween(0));
    }

    @Test
    void testAdjustedRightMinutesUtc_NoAdjustment_UTC() {
        val pair = pair(UTC, 8, 17);
        assertEquals(pair.right().totalMinutesInUtc(), pair.adjustedRightMinutesUtc());
    }

    @Test
    void testAdjustedRightMinutesUtc_Adjustment_UTC() {
        val pair = pair(UTC, 18, 4);
        assertEquals(pair.right().totalMinutesInUtc() + (24 * 60), pair.adjustedRightMinutesUtc());
    }

    @Test
    void testAdjustedRightMinutesUtc_Adjustment_NYC() {
        val pair = pair(NYC, 18, 4);
        assertEquals(pair.right().totalMinutesInUtc() + (24 * 60), pair.adjustedRightMinutesUtc());
    }

    @Test
    void splitAround_UTC() {
        val expected = List.of(pair(UTC, 8, 10), pair(UTC, 10, 17));
        assertEquals(expected, pair(UTC, 8, 17).splitAround(10));
    }

    @Test
    void splitAround_NYC_At_0() {
        val expected = List.of(pair(NYC, 18, 20), pair(NYC, 20, 4));
        assertEquals(expected, pair(NYC, 18, 4).splitAround(0));
    }

    @Test
    void splitAround_NYC_At_17() {
        val expected = List.of(pair(NYC, 8, 13), pair(NYC, 13, 17));
        assertEquals(expected, pair(NYC, 8, 17).splitAround(17));
    }

    @Test
    void splitAroundNYCOutsideNoSplit() {
        val expected = List.of(pair(NYC, 8, 17));
        assertEquals(expected, pair(NYC, 8, 17).splitAround(10));
    }

    private static TimePair pair(Zone zone, int left, int right) {
        return new TimePair(Time.at(zone, left), Time.at(zone, right));
    }
}