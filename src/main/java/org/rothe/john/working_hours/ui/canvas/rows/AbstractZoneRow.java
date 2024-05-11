package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.Getter;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.util.Palette;


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

    protected int timeToColumnCenter(Time time) {
        return getCanvasInfo().timeToColumnCenter(time.totalMinutesInUtc());
    }
}
