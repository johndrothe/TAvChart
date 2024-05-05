package org.rothe.john.working_hours.model;

import lombok.With;

import java.time.LocalTime;

@With
public record Availability(LocalTime normalStart, LocalTime normalEnd, LocalTime lunchStart, LocalTime lunchEnd) {
    public static Availability standard() {
        return new Availability(
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0)
        );
    }
}
