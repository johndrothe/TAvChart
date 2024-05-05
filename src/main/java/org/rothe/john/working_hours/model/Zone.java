package org.rothe.john.working_hours.model;

import lombok.val;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;

public class Zone {
    public static final int MINUTES_IN_A_DAY = 24 * 60;
    private static final DateTimeFormatter TRANSITION_FORMATTER = DateTimeFormatter.ofPattern("LLL dd, yyyy");
    private final ZoneId zoneId;

    public Zone(ZoneId zoneId) {
        this.zoneId = zoneId;
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

    public static Zone[] getAvailableZones() {
        return ZoneOffset.getAvailableZoneIds().stream()
                .sorted()
                .map(ZoneId::of)
                .map(Zone::new)
                .toArray(Zone[]::new);
    }

    public ZoneRules getRules() {
        return zoneId.getRules();
    }

    public boolean isFixedOffset() {
        return zoneId.getRules().isFixedOffset();
    }

    public String getAbbrevAndOffset() {
        return String.format("%s (%s)", getAbbreviation(), getOffsetHours());
    }

    public String getAbbreviation() {
        val zone = TimeZone.getTimeZone(zoneId);
        return zone.getDisplayName(zone.inDaylightTime(new Date()), TimeZone.SHORT);
    }

    @Override
    public int hashCode() {
        return zoneId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Zone z2) {
            return zoneId.equals(z2.zoneId);
        }
        return false;
    }

    public String getId() {
        if (zoneId.equals(ZoneOffset.UTC)) {
            return "GMT/Greenwich";
        }
        // return zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        return zoneId.getId();
    }

    public String getRawZoneId() {
        if (zoneId.equals(ZoneOffset.UTC)) {
            return "UTC";
        }
        return zoneId.getId();
    }

    public int getOffsetHours() {
        return toHours(getOffsetSeconds());
    }

    public int getOffsetSeconds() {
        return zoneId.getRules().getOffset(Instant.now()).get(OFFSET_SECONDS);
    }

    public int toMinutesUtc(LocalTime time) {
        return normalizeHour(time.getHour() - getOffsetHours()) * 60 + time.getMinute();
    }

    public String toTransitionDateStr(ZoneRules rules) {
        ZoneOffsetTransition next = rules.nextTransition(Instant.now());
        ZoneOffsetTransition prev = rules.previousTransition(Instant.now());

        return String.format("(%s   →   %s)",
                prev.getDateTimeBefore().format(TRANSITION_FORMATTER),
                next.getDateTimeAfter().format(TRANSITION_FORMATTER));
    }

    private static int toHours(int seconds) {
        return (int) Math.round(seconds / 60.0 / 60.0);
    }

    public static Zone fromCsv(String id) {
        try {
            return new Zone(ZoneId.of(id));
        } catch (DateTimeException dte) {
            System.err.println("Invalid Zone ID '" + id + "':  Using default '" + ZoneId.systemDefault() + "'");
            dte.printStackTrace();
        }
        return new Zone(ZoneId.systemDefault());
    }

    @Override
    public String toString() {
        return zoneId.toString();
    }
}
