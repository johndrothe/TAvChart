package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;
import org.rothe.john.team_schedule.util.Palette;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.ZoneId;

public class TimeZoneRenderer extends ZonedRenderer {
    private static final int TEXT_MARGIN = 5;
    private final Color lineHintColor;

    public TimeZoneRenderer(CanvasInfo canvasInfo, ZoneId zoneId, Palette palette) {
        super(canvasInfo, zoneId, palette);
        lineHintColor = getLineHintColor();
    }

    @Override
    protected int getRendererRightLocation() {
        return getWidth() - getRowFooterWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;
        val hourColumnWidth = getHourColumnWidth();
        val offset = getUtcOffset();

        paintRowHeader(g2d);

        for (int hourUtc = 0; hourUtc < 24; ++hourUtc) {
            paintTimeZone(g2d, hourUtc, offset, hourColumnWidth);
        }

        paintRowFooter(g2d);
    }

    private void paintRowHeader(Graphics2D g2d) {
        g2d.setColor(getTextColor());
        drawRightJustified(g2d, getZoneIdString(), 0, getRowHeaderWidth() - TEXT_MARGIN);
    }

    private void paintTimeZone(Graphics2D g2d, int hourUtc, int offset, double hourColumnWidth) {
        val x = timeToColumnStart(hourUtc * 60);

        g2d.setColor(lineHintColor);
        g2d.drawLine(x, 6, x, getHeight() - 6);

        g2d.setColor(getTextColor());
        val hourStr = Integer.toString(normalizeHour(hourUtc + offset));
        drawCentered(g2d, hourStr, x, hourColumnWidth);
    }

    private void paintRowFooter(Graphics2D g2d) {
        g2d.setColor(getTextColor());
        drawLeftJustified(g2d, getLocationDisplayString(),
                timeToColumnStart(24 * 60) + TEXT_MARGIN);
    }
}
