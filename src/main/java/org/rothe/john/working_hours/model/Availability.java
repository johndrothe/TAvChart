package org.rothe.john.working_hours.model;

import lombok.With;

import java.time.LocalTime;

@With
public record Availability(Period normal, Period lunch) {
    public static Availability standard() {
        return new Availability(
                new Period(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new Period(LocalTime.of(12, 0), LocalTime.of(13, 0))
        );
    }
}
