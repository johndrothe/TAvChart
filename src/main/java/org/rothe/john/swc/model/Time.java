package org.rothe.john.swc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.val;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Time {
    public static final int MINUTES_IN_A_DAY = 24 * 60;
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
        return of(zone, LocalTime.of(hour + minute / 60, minute % 60));
    }

    public static Time of(Zone zone, LocalTime time) {
        return new Time(zone, time);
    }

    public static Time fromHoursUtc(Zone zone, int hoursUtc) {
        return of(zone, fromUtc(zone, hoursUtc));
    }

    private Time(Zone zone, LocalTime local) {
        this.zone = zone;
        this.local = local.withSecond(0).withNano(0);
        this.utc = toUtc(zone, local);
    }

    public static String toClockFormat(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

    public static int normalizeHour(int hour) {
        int h = hour % 24;
        if (h < 0) {
            return h + 24;
        }
        return h;
    }

    public static int normalizeMinutes(int minutes) {
        int m = minutes % MINUTES_IN_A_DAY;
        if (m < 0) {
            return m + MINUTES_IN_A_DAY;
        }
        return m;
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

    @JsonCreator // constructor can be public, private, whatever
    private Time(@JsonProperty("zone") Zone zone, @JsonProperty("local") String local) {
        this(zone, LocalTime.parse(local));
    }

    @JsonProperty("local")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
    private LocalTime local() {
        return local;
    }

    public Time inUtc() {
        return Time.at(Zone.utc(), hourUtc(), minuteUtc());
    }

    private static ZonedDateTime toUtc(Zone zone, LocalTime localTime) {
        return ZonedDateTime.of(LocalDate.now(), localTime, zone.getRawZoneId())
                .withZoneSameInstant(ZoneOffset.UTC);
    }

    private static LocalTime fromUtc(Zone zone, int hourUtc) {
        return ZonedDateTime.of(LocalDate.now(), LocalTime.of(hourUtc, 0), ZoneOffset.UTC)
                .withZoneSameInstant(zone.getRawZoneId())
                .toLocalTime();
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Time t) {
            return Objects.equals(local, t.local) && Objects.equals(zone, t.zone);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(local, zone);
    }

    public Time addHours(int hours) {
        return Time.at(zone, normalizeHour(hourLocal() + hours), minuteLocal());
    }
}
