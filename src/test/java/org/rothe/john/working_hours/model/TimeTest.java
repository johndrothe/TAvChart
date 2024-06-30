package org.rothe.john.working_hours.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeTest {
    private final Zone UTC = Zone.fromCsv("Z");
    private final Zone NYC = Zone.fromCsv("America/New_York");
    private final Zone CHI = Zone.fromCsv("America/Chicago");
    private final Zone LAX = Zone.fromCsv("America/Los_Angeles");
    private final Zone PL = Zone.fromCsv("Poland");


    @Test
    void fromHoursUtc() {
        assertEquals(Time.at(UTC, 10), Time.fromHoursUtc(UTC, 10));
        assertEquals(Time.at(NYC, 6), Time.fromHoursUtc(NYC, 10));
        assertEquals(Time.at(CHI, 5), Time.fromHoursUtc(CHI, 10));
        assertEquals(Time.at(LAX, 3), Time.fromHoursUtc(LAX, 10));
        assertEquals(Time.at(PL, 12), Time.fromHoursUtc(PL, 10));
    }

    @Test
    void normalizeHours() {
        assertEquals(0, Time.normalizeHour(0));
        assertEquals(1, Time.normalizeHour(1));
        assertEquals(11, Time.normalizeHour(11));
        assertEquals(23, Time.normalizeHour(23));
    }

    @Test
    void normalizeHoursOver24() {
        assertEquals(0, Time.normalizeHour(24));
        assertEquals(1, Time.normalizeHour(24 + 1));
        assertEquals(11, Time.normalizeHour(24 + 11));
        assertEquals(23, Time.normalizeHour(24 + 23));
    }

    @Test
    void normalizeHoursBelowZero() {
        assertEquals(0, Time.normalizeHour(-24));
        assertEquals(1, Time.normalizeHour(1 - 24));
        assertEquals(11, Time.normalizeHour(11 - 24));
        assertEquals(23, Time.normalizeHour(23 - 24));
    }

    @Test
    void normalizeMinutes() {
        assertEquals(0, Time.normalizeMinutes(0));
        assertEquals(60, Time.normalizeMinutes(60));
        assertEquals(11 * 60, Time.normalizeMinutes(11 * 60));
        assertEquals(23 * 60, Time.normalizeMinutes(23 * 60));
    }

    @Test
    void normalizeMinutesOver24Hours() {
        assertEquals(0, Time.normalizeMinutes((24) * 60));
        assertEquals(60, Time.normalizeMinutes((24 + 1) * 60));
        assertEquals(11 * 60, Time.normalizeMinutes((24 + 11) * 60));
        assertEquals(23 * 60, Time.normalizeMinutes((24 + 23) * 60));
    }

    @Test
    void normalizeMinutesBelowZero() {
        assertEquals(0, Time.normalizeMinutes((-24) * 60));
        assertEquals(60, Time.normalizeMinutes((1 - 24) * 60));
        assertEquals(11 * 60, Time.normalizeMinutes((11 - 24) * 60));
        assertEquals(23 * 60, Time.normalizeMinutes((23 - 24) * 60));
    }
}