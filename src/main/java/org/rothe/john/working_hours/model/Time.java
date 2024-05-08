package org.rothe.john.working_hours.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Time {
    private final LocalTime time;

    public static Time parse(String text) {
        return of(LocalTime.parse(text));
    }

    public static Time at(int hour) {
        return at(hour, 0);
    }

    public static Time at(int hour, int minute) {
        return of(LocalTime.of(hour, minute));
    }

    public static Time of(LocalTime time) {
        return new Time(time);
    }

    private Time(LocalTime time) {
        this.time = time;
    }

    public ZonedDateTime atZone(Zone zone) {
        return ZonedDateTime.of(LocalDate.now(), time, zone.getRawZoneId());
    }

    public int hour() {
        return time.getHour();
    }

    public int minute() {
        return time.getMinute();
    }

    public String toString() {
        return time.toString();
    }

}
