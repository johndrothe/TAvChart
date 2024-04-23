package org.rothe.john.working_hours.util;

import lombok.val;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;

// TODO: Convert this to a Zone class that wraps and replaces ZoneId to provide additional features.

public abstract class Zones {
    private static final DateTimeFormatter TRANSITION_FORMATTER = DateTimeFormatter.ofPattern("LLL dd, yyyy");

    private Zones() {

    }

    public static String getZoneIdString(ZoneId zoneId) {
        return String.format("%s (%s)", getZoneAbbrev(zoneId), getUtcOffset(zoneId));
    }

    public static String getZoneAbbrev(ZoneId zoneId) {
        val zone = TimeZone.getTimeZone(zoneId);
        return zone.getDisplayName(zone.inDaylightTime(new Date()), TimeZone.SHORT);
    }

    public static String getLocationDisplayString(ZoneId zoneId) {
        if (zoneId.equals(ZoneOffset.UTC)) {
            return "GMT/Greenwich";
        }
        // return zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        return zoneId.getId();
    }

    public static int getUtcOffset(ZoneId zoneId) {
        return toHours(zoneId.getRules().getOffset(Instant.now()).get(OFFSET_SECONDS));
    }

    public static String toTransitionDateStr(ZoneRules rules) {
        ZoneOffsetTransition next = rules.nextTransition(Instant.now());
        ZoneOffsetTransition prev = rules.previousTransition(Instant.now());

        return String.format("(%s   →   %s)",
                prev.getDateTimeBefore().format(TRANSITION_FORMATTER),
                next.getDateTimeAfter().format(TRANSITION_FORMATTER));
    }

    private static int toHours(int seconds) {
        return (int)Math.round(seconds / 60.0 / 60.0);
    }
}
