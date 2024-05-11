package org.rothe.john.working_hours.model;

import lombok.val;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Time {
    private final LocalTime local;
    private final ZonedDateTime utc;
    private final Zone zone;

    public static Time parse(Zone zone, String text) {
        return of(zone, LocalTime.parse(text));
    }

    public static Time at(Zone zone, int hour) {
        return at(zone, hour, 0);
    }

    public static Time at(Zone zone, int hour, int minute) {
        return of(zone, LocalTime.of(hour, minute));
    }

    public static Time of(Zone zone, LocalTime time) {
        return new Time(zone, time);
    }

    private Time(Zone zone, LocalTime local) {
        this.zone = zone;
        this.local = local;
        this.utc = toUtc(zone, local);
    }

    public int hourLocal() {
        return local.getHour();
    }

    public int minuteLocal() {
        return local.getMinute();
    }

    public int hourUtc() {
        return utc.getHour();
    }

    public int minuteUtc() {
        return utc.getMinute();
    }

    public int totalMinutesInUtc() {
        return hourUtc() * 60 + minuteUtc();
    }

    public String toString() {
        return local.toString();
    }

    public String toUtcString() {
        return String.format("%02d:%02d", utc.getHour(), utc.getMinute());
    }

    public Zone zone() {
        return zone;
    }

    private static ZonedDateTime toUtc(Zone zone, LocalTime localTime) {
        return ZonedDateTime.of(LocalDate.now(), localTime, zone.getRawZoneId())
                .withZoneSameInstant(ZoneOffset.UTC);
    }

    public static int toHours(int seconds) {
        return (int) Math.round(seconds / 60.0 / 60.0);
    }

    public Time roundToQuarterHour() {
        return Time.at(zone(), hourLocal(), roundToQuarterHour(minuteLocal()));
    }

    private static int roundToQuarterHour(int minutes) {
        val mod = minutes % 15;
        if (mod > 7) {
            return minutes - mod + 15;
        }
        return minutes - mod;
    }
}
