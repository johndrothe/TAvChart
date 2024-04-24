package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.Getter;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.util.Palette;
import org.rothe.john.working_hours.model.Zone;

import java.time.LocalTime;


@Getter
public abstract class AbstractZoneRow extends CanvasRow {
    private final Zone zone;

    protected AbstractZoneRow(CanvasInfo canvasInfo, Zone zone, Palette palette) {
        super(canvasInfo, palette.fill(zone), palette.line(zone));
        this.zone = zone;
    }

    public String getRowHeader() {
        return zone.getAbbrevAndOffset();
    }

    public String getRowFooter() {
        return zone.getId();
    }

    public int getOffsetHours() {
        return zone.getOffsetHours();
    }

    protected static int normalizeHour(int hour) {
        int h = hour % 24;
        if (h < 0) {
            return h + 24;
        }
        return h;
    }

    protected int timeToColumnCenter(LocalTime time) {
        return getCanvasInfo().timeToColumnCenter(toMinutesUtc(time));
    }

    private int toMinutesUtc(LocalTime time) {
        return normalizeHour(time.getHour() - getOffsetHours()) * 60 + time.getMinute();
    }
}
