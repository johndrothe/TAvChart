package org.rothe.john.working_hours.model;

import lombok.With;

@With
public record Availability(Time normalStart, Time normalEnd, Time lunchStart, Time lunchEnd) {
    public static Availability standard() {
        return new Availability(
                Time.at(8), Time.at(17),
                Time.at(12), Time.at(13)
        );
    }
}
