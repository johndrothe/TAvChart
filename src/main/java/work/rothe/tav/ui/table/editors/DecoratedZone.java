package work.rothe.tav.ui.table.editors;

import work.rothe.tav.model.Zone;

import java.time.ZoneId;

public class DecoratedZone extends Zone {
    private final String name;

    public DecoratedZone(ZoneId zoneId, String name) {
        super(zoneId);
        this.name = name;
    }

    public Zone toZone() {
        return new Zone(getRawZoneId());
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, super.toString());
    }
}
