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
    void testAccessors() {
        Time t = Time.at(NYC, 11, 22);
        assertEquals(11, t.hourLocal());
        assertEquals(22, t.minuteLocal());

        assertEquals(15, t.hourUtc());
        assertEquals(22, t.minuteUtc());
    }

    @Test
    void testParseTime() {
        assertEquals(Time.at(UTC, 11, 22), Time.parse(UTC, "11:22:00"));
        assertEquals(Time.at(NYC, 11, 22), Time.parse(NYC, "11:22:00"));
    }

    @Test
    void testParseTimeByZone() {
        assertEquals(Time.at(UTC, 10), Time.parse(UTC, "10:00:00"));
        assertEquals(Time.at(NYC, 10), Time.parse(NYC, "10:00:00"));
        assertEquals(Time.at(CHI, 10), Time.parse(CHI, "10:00:00"));
        assertEquals(Time.at(LAX, 10), Time.parse(LAX, "10:00:00"));
        assertEquals(Time.at(PL, 10), Time.parse(PL, "10:00:00"));
    }

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

    @Test
    void testToUtcString() {
        assertEquals("11:22", Time.at(UTC, 11, 22).toUtcString());
    }

    @Test
    void testInUtc() {
        Time result = Time.at(NYC, 15, 22).inUtc();
        assertEquals(Zone.utc(), result.zone());
        assertEquals(19, result.hourLocal());
        assertEquals(19, result.hourUtc());
    }

    @Test
    void testToHours(){
        assertEquals(0, Time.toHours(0));
        assertEquals(0, Time.toHours(1));
        assertEquals(0, Time.toHours(60));
        assertEquals(1, Time.toHours(60*60));
        assertEquals(10, Time.toHours(60*60*10));
    }

    @Test
    void testRoundDownToQuarterHour() {
        assertEquals(Time.at(UTC, 3, 0), Time.at(UTC, 2, 59).roundToQuarterHour());
        assertEquals(Time.at(UTC, 3, 15), Time.at(UTC, 3, 14).roundToQuarterHour());
        assertEquals(Time.at(UTC, 3, 30), Time.at(UTC, 3, 29).roundToQuarterHour());
        assertEquals(Time.at(UTC, 3, 45), Time.at(UTC, 3, 44).roundToQuarterHour());
        assertEquals(Time.at(UTC, 4, 0), Time.at(UTC, 3, 59).roundToQuarterHour());
    }

    @Test
    void testRoundToQuarterHour15() {
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 10).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 11).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 12).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 13).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 14).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 15).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 16).roundToQuarterHour());
        assertEquals(Time.at(UTC, 1, 15), Time.at(UTC, 1, 17).roundToQuarterHour());
    }
}