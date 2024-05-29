package org.rothe.john.working_hours.ui.canvas.st;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.Zone;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimePairTest {
    private final Zone UTC = Zone.fromCsv("Z");
    private final Zone EDT = Zone.fromCsv("-04:00");
    private final Zone CHI = Zone.fromCsv("America/Chicago");
    private final Zone LAX = Zone.fromCsv("America/Los_Angeles");
    private final Zone PL = Zone.fromCsv("Poland");


    @Test
    void testIsSplitBetween() {
        assertTrue(pair(EDT, 18, 4).isSplitBetween(0));
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
    void testAdjustedRightMinutesUtc_Adjustment_EDT() {
        val pair = pair(EDT, 18, 4);
        assertEquals(pair.right().totalMinutesInUtc() + (24 * 60), pair.adjustedRightMinutesUtc());
    }

    @Test
    void splitAround_UTC() {
        val expected = List.of(pair(UTC, 8, 10), pair(UTC, 10, 17));
        assertEquals(expected, pair(UTC, 8, 17).splitAround(10));
    }

    @Test
    void splitAround_EDT_At_0() {
        val expected = List.of(pair(EDT, 18, 20), pair(EDT, 20, 4));
        assertEquals(expected, pair(EDT, 18, 4).splitAround(0));
    }

    @Test
    void splitAround_EDT_At_17() {
        val expected = List.of(pair(EDT, 8, 13), pair(EDT, 13, 17));
        assertEquals(expected, pair(EDT, 8, 17).splitAround(17));
    }

    @Test
    void splitAroundEDTOutsideNoSplit() {
        val expected = List.of(pair(EDT, 8, 17));
        assertEquals(expected, pair(EDT, 8, 17).splitAround(10));
    }

    @Test
    void contains_Same_Zone_UTC() {
        val expected = pair(UTC, 8, 17);
        assertFalse(expected.contains(Time.at(UTC, 7)));
        assertFalse(expected.contains(Time.at(UTC, 7, 59)));
        assertTrue(expected.contains(Time.at(UTC, 8)));
        assertTrue(expected.contains(Time.at(UTC, 10)));
        assertTrue(expected.contains(Time.at(UTC, 16)));
        assertTrue(expected.contains(Time.at(UTC, 16, 59)));
        assertFalse(expected.contains(Time.at(UTC, 17)));
    }

    @Test
    void contains_Same_Zone_EDT() {
        val expected = pair(EDT, 8, 17);
        assertFalse(expected.contains(Time.at(EDT, 7)));
        assertFalse(expected.contains(Time.at(EDT, 7, 59)));
        assertTrue(expected.contains(Time.at(EDT, 8)));
        assertTrue(expected.contains(Time.at(EDT, 10)));
        assertTrue(expected.contains(Time.at(EDT, 16)));
        assertTrue(expected.contains(Time.at(EDT, 16, 59)));
        assertFalse(expected.contains(Time.at(EDT, 17)));
    }

    @Test
    void contains_UTC_EDT() {
        val expected = pair(UTC, 8, 17);
        assertFalse(expected.contains(Time.at(EDT, 3)));
        assertFalse(expected.contains(Time.at(EDT, 3, 59)));
        assertTrue(expected.contains(Time.at(EDT, 4)));
        assertTrue(expected.contains(Time.at(EDT, 6)));
        assertTrue(expected.contains(Time.at(EDT, 12)));
        assertTrue(expected.contains(Time.at(EDT, 12, 59)));
        assertFalse(expected.contains(Time.at(EDT, 13)));
    }

    @Test
    void contains_EDT_UTC() {
        val expected = pair(EDT, 8, 17);
        assertFalse(expected.contains(Time.at(UTC, 11)));
        assertFalse(expected.contains(Time.at(UTC, 11, 59)));
        assertTrue(expected.contains(Time.at(UTC, 12)));
        assertTrue(expected.contains(Time.at(UTC, 14)));
        assertTrue(expected.contains(Time.at(UTC, 20)));
        assertTrue(expected.contains(Time.at(UTC, 20, 59)));
        assertFalse(expected.contains(Time.at(UTC, 21)));
    }

    @Test
    void duration_Simple_UTC() {
        assertEquals(Duration.ofHours(1), pair(UTC, 8, 9).duration());
        assertEquals(Duration.ofHours(2), pair(UTC, 8, 10).duration());
    }

    @Test
    void duration_Simple_EDT() {
        assertEquals(Duration.ofHours(1), pair(EDT, 8, 9).duration());
        assertEquals(Duration.ofHours(2), pair(EDT, 8, 10).duration());
    }

    @Test
    void duration_Wrap() {
        assertEquals(Duration.ofHours(8), pair(UTC, 18, 2).duration());
        assertEquals(Duration.ofHours(8), pair(EDT, 18, 2).duration());
    }

    private static TimePair pair(Zone zone, int left, int right) {
        return new TimePair(Time.at(zone, left), Time.at(zone, right));
    }
}