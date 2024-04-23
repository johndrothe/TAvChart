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

public class Zone {
    private static final DateTimeFormatter TRANSITION_FORMATTER = DateTimeFormatter.ofPattern("LLL dd, yyyy");
    private final ZoneId zoneId;

    public Zone(ZoneId zoneId) {
        this.zoneId = zoneId;
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

    public String getId() {
        if (zoneId.equals(ZoneOffset.UTC)) {
            return "GMT/Greenwich";
        }
        // return zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        return zoneId.getId();
    }

    public int getOffsetHours() {
        return toHours(getOffsetSeconds());
    }
    public int getOffsetSeconds() {
        return zoneId.getRules().getOffset(Instant.now()).get(OFFSET_SECONDS);
    }

    public String toTransitionDateStr(ZoneRules rules) {
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
