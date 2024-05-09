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

    public static Availability standard(int startHour) {
        return new Availability(
                Time.at(startHour),
                Time.at(startHour + 9),
                Time.at(startHour + 4),
                Time.at(startHour + 5));
    }
}
