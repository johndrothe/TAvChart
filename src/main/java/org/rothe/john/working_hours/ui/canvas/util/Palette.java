package org.rothe.john.working_hours.ui.canvas.util;

import org.rothe.john.working_hours.model.Zone;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.Color.decode;

public class Palette {
    private final Map<Zone, Color> borderMap;
    private final Map<Zone, Color> fillMap;

    public Palette(List<Zone> zones) {
        borderMap = mapZones(zones, borderColors());
        fillMap = mapZones(zones, fillColors());
    }

    public Color fill(Zone zone) {
        return fillMap.get(zone);
    }

    public Color line(Zone zone) {
        return borderMap.get(zone);
    }

    private Map<Zone, Color> mapZones(List<Zone> zones, List<Color> colors) {
        if (colors.isEmpty()) {
            throw new IllegalArgumentException("Colors cannot be empty.");
        }

        var map = new HashMap<Zone, Color>();
        var color = colors.iterator();
        for(Zone zone : zones) {
            if(!color.hasNext()) {
                color = colors.iterator();
            }
            map.put(zone, color.next());
        }

        return map;
    }

    private static List<Color> fillColors() {
        return List.of(
                decode("#DAE8FC"), // blue
                decode("#D5E8D4"), // green
                decode("#FFE6CC"), // orange
                decode("#E1D5E7"), // purple
                decode("#FFF2CC"), // beige
                decode("#F8CECC")  // red
        );
    }

    private static List<Color> borderColors() {
        return List.of(
                decode("#6C8EBF"), // blue
                decode("#82B366"), // green
                decode("#D79B00"), // orange
                decode("#9673A6"), // purple
                decode("#D6B656"), // beige
                decode("#B85450")  // red
        );
    }
}
