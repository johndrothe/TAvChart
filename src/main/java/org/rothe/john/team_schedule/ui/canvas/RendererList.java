package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class RendererList {
    private final List<AbstractRenderer> renderers = new ArrayList<>();

    public void clear() {
        renderers.clear();
    }

    public void add(AbstractRenderer renderer) {
        renderers.add(renderer);
    }

    public boolean isEmpty() {
        return renderers.isEmpty();
    }

    public AbstractRenderer getFirst() {
        return renderers.getFirst();
    }

    public void updateColumnWidths(Graphics2D g2d) {
        val header = calculateColumnWidth(ZonedRenderer::getZoneIdString, g2d);
        val footer = calculateColumnWidth(ZonedRenderer::getLocationDisplayString, g2d);

        renderers.forEach(r -> updateColumnWidths(r, header, footer));
    }

    private void updateColumnWidths(AbstractRenderer renderer, int header, int footer) {
        renderer.setRowHeaderWidth(header);
        renderer.setRowFooterWidth(footer);
    }

    private int calculateColumnWidth(Function<ZonedRenderer, String> getter, Graphics2D g2d) {
        return getZonedRenderers()
                .map(getter)
                .map(s -> getRenderedStringWidth(s, g2d))
                .max(Comparator.naturalOrder())
                .orElse(0) + 20;
    }

    private Stream<ZonedRenderer> getZonedRenderers() {
        return renderers.stream()
                .filter(r -> r instanceof ZonedRenderer)
                .map(r -> (ZonedRenderer) r);
    }

    private static int getRenderedStringWidth(String text, Graphics2D g) {
        return (int) Math.ceil(g.getFontMetrics().getStringBounds(text, g).getWidth());
    }
}
