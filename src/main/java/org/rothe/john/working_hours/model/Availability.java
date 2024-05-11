package org.rothe.john.working_hours.model;

import lombok.With;

@With
public record Availability(Time normalStart, Time normalEnd, Time lunchStart, Time lunchEnd) {
    public static Availability businessHours(Zone zone) {
        return new Availability(
                Time.at(zone, 8), Time.at(zone, 17),
                Time.at(zone, 12), Time.at(zone, 13)
        );
    }
}
