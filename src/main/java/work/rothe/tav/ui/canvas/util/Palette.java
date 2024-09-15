package work.rothe.tav.ui.canvas.util;

import lombok.val;
import work.rothe.tav.model.Zone;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.awt.Color.decode;

public class Palette {
    private static final List<ColorPair> COLORS = colors();
    private final Map<Zone, ColorPair> colorMap;

    public Palette(List<Zone> zones) {
        colorMap = mapZones(zones);
    }

    public Color fill(Zone zone) {
        return colorMap.get(zone).fill();
    }

    public Color line(Zone zone) {
        return colorMap.get(zone).line();
    }

    private Map<Zone, ColorPair> mapZones(List<Zone> zones) {
        val map = new HashMap<Zone, ColorPair>();
        val colorIterator = colorsIterator();
        for(Zone zone : zones) {
            map.put(zone, colorIterator.next());
        }

        return map;
    }

    private Iterator<ColorPair> colorsIterator() {
        return Stream.generate(COLORS::stream).flatMap(s -> s).iterator();
    }

    private static List<ColorPair> colors() {
        return List.of(
                new ColorPair("#DAE8FC", "#6C8EBF"), // blue
                new ColorPair("#D5E8D4", "#82B366"), // green
                new ColorPair("#FFE6CC", "#D79B00"), // orange
                new ColorPair("#E1D5E7", "#9673A6"), // purple
                new ColorPair("#FFF2CC", "#D6B656"), // beige
                new ColorPair("#F8CECC", "#B85450")  // red
        );
    }

    private record ColorPair (Color fill, Color line) {
        ColorPair(String fill, String line) {
            this(decode(fill), decode(line));
        }
    }
}
