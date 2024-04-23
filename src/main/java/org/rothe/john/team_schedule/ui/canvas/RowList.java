package org.rothe.john.team_schedule.ui.canvas;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class RowList {
    private final List<CanvasRow> rows = new ArrayList<>();

    public void clear() {
        rows.clear();
    }

    public void add(CanvasRow row) {
        rows.add(row);
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public CanvasRow getFirst() {
        return rows.getFirst();
    }

    public int getColumnHeaderWidth(Graphics2D g2d) {
        return calculateColumnWidth(ZoneIdRow::getZoneIdString, g2d);
    }

    public int getColumnFooterWidth(Graphics2D g2d) {
        return calculateColumnWidth(ZoneIdRow::getLocationDisplayString, g2d);
    }

    private int calculateColumnWidth(Function<ZoneIdRow, String> getter, Graphics2D g2d) {
        return getZoneIdRows()
                .map(getter)
                .map(s -> getRenderedStringWidth(s, g2d))
                .max(Comparator.naturalOrder())
                .orElse(0) + 20;
    }

    private Stream<ZoneIdRow> getZoneIdRows() {
        return rows.stream()
                .filter(r -> r instanceof ZoneIdRow)
                .map(r -> (ZoneIdRow) r);
    }

    private static int getRenderedStringWidth(String text, Graphics2D g) {
        return (int) Math.ceil(g.getFontMetrics().getStringBounds(text, g).getWidth());
    }
}
