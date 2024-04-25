package org.rothe.john.working_hours.model;

import java.time.LocalTime;

public record Period(LocalTime start, LocalTime end) {
    public static Period fromCsv(String start, String end) {
        return new Period(LocalTime.parse(start), LocalTime.parse(end));
    }
}
