package org.rothe.john.team_schedule.model;

import java.time.LocalTime;

public record Availability(LocalTime start, LocalTime end, LocalTime lunchStart, LocalTime lunchEnd) {
    public static Availability standard() {
        return new Availability(
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0)
        );
    }
}
