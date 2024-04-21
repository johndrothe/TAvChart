package org.rothe.john.team_schedule.ui.canvas;

import org.rothe.john.team_schedule.util.Palette;
import lombok.Getter;
import lombok.val;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;

@Getter
public abstract class ZonedRenderer extends AbstractRenderer {
    private final ZoneId zoneId;

    protected ZonedRenderer(ZoneId zoneId, Palette palette) {
        super(palette.fill(zoneId), palette.line(zoneId));
        this.zoneId = zoneId;
    }

    public String getZoneIdString() {
        return String.format("%s (%s)", getZoneAbbrev(), getUtcOffset());
    }

    public String getZoneAbbrev() {
        val zone = TimeZone.getTimeZone(zoneId);
        return zone.getDisplayName(zone.inDaylightTime(new Date()), TimeZone.SHORT);
    }

    public String getLocationDisplayString() {
        if (zoneId.equals(ZoneOffset.UTC)) {
            return "UTC/Greenwich";
        }
        return zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
    }

    public int getUtcOffset() {
        return toHours(zoneId.getRules().getOffset(Instant.now()).get(OFFSET_SECONDS));
    }

    private static int toHours(int seconds) {
        return (int)Math.round(seconds / 60.0 / 60.0);
    }

    protected static int normalizeHour(int hour) {
        int h = hour % 24;
        if(h < 0) {
            return h + 24;
        }
        return h;
    }

    protected int timeToColumnStart(LocalTime time) {
        return timeToColumnStart(toMinutesUtc(time));
    }

    protected int timeToColumnCenter(LocalTime time) {
        return timeToColumnCenter(toMinutesUtc(time));
    }

    private int toMinutesUtc(LocalTime time) {
        return normalizeHour(time.getHour() - getUtcOffset()) * 60 + time.getMinute();
    }
}
