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
}