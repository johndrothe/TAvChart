package org.rothe.john.swc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.val;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Zone {
    private static final DateTimeFormatter TRANSITION_FORMATTER = DateTimeFormatter.ofPattern("LLL dd, yyyy");
    private final ZoneId zoneId;

    public Zone(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @JsonCreator
    private Zone(@JsonProperty("id") String id)
    {
        this(ZoneId.of(id));
    }

    public static Zone here() {
        return new Zone(ZoneId.systemDefault());
    }

    public static Zone utc() {
        return new Zone(ZoneOffset.UTC);
    }

    public static Zone[] getAvailableZones() {
        return ZoneOffset.getAvailableZoneIds().stream()
                .sorted()
                .map(ZoneId::of)
                .map(Zone::new)
                .toArray(Zone[]::new);
    }

    @JsonIgnore
    public ZoneRules getRules() {
        return zoneId.getRules();
    }

    @JsonIgnore
    public String getAbbrevAndOffset() {
        return String.format("%s (%s)", getAbbreviation(), getOffsetHours());
    }

    @JsonIgnore
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
            return "UTC";
        }
        // return zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        return zoneId.getId();
    }

    @JsonIgnore
    public ZoneId getRawZoneId() {
        return zoneId;
    }

    @JsonIgnore
    public int getOffsetHours() {
        return Time.toHours(getOffsetSeconds());
    }

    @JsonIgnore
    public int getOffsetSeconds() {
        return zoneId.getRules().getOffset(Instant.now()).get(OFFSET_SECONDS);
    }

    public String toTransitionDateStr(ZoneRules rules) {
        ZoneOffsetTransition next = rules.nextTransition(Instant.now());
        ZoneOffsetTransition prev = rules.previousTransition(Instant.now());

        return String.format("(%s   →   %s)", format(prev), format(next));
    }

    public boolean hasTransitions() {
        val rules = zoneId.getRules();
        return nonNull(rules.previousTransition(Instant.now()))
                && nonNull(rules.nextTransition(Instant.now()));
    }

    private static String format(ZoneOffsetTransition transition) {
        if(isNull(transition)) {
            return "None";
        }
        return transition.getDateTimeBefore().format(TRANSITION_FORMATTER);
    }

    public static Zone fromCsv(String id) {
        try {
            return new Zone(ZoneId.of(id));
        } catch (DateTimeException dte) {
            System.err.println("Warning: Invalid Zone ID '" + id + "':  Using default '" + ZoneId.systemDefault() + "'");
            dte.printStackTrace();
        }
        return new Zone(ZoneId.systemDefault());
    }

    @Override
    public String toString() {
        return zoneId.toString();
    }
}
