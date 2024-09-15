package work.rothe.tav.ui.canvas.rows;

import lombok.Getter;
import work.rothe.tav.model.Time;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.canvas.util.Palette;


@Getter
public abstract class AbstractZoneRow extends CanvasRow {
    private final Zone zone;

    protected AbstractZoneRow(CanvasCalculator calculator, Zone zone, Palette palette) {
        super(calculator, palette.fill(zone), palette.line(zone));
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
        return getCalculator().toColumnCenter(time);
    }
}
