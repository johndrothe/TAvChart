package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.util.Palette;
import org.rothe.john.working_hours.util.Zone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ZoneRow extends AbstractZoneRow {
    private static final int TEXT_MARGIN = 5;
    private final Color lineHintColor;

    public ZoneRow(CanvasInfo canvasInfo, Zone zone, Palette palette) {
        super(canvasInfo, zone, palette);
        lineHintColor = getLineHintColor();
    }

    @Override
    protected int getRowRightLocation() {
        return getWidth() - getRowFooterWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;
        val hourColumnWidth = getHourColumnWidth();
        val offset = getOffsetHours();

        paintRowHeader(g2d);

        for (int hourUtc = 0; hourUtc < 24; ++hourUtc) {
            paintTimeZone(g2d, hourUtc, offset, hourColumnWidth);
        }

        paintRowFooter(g2d);
    }

    private void paintRowHeader(Graphics2D g2d) {
        g2d.setColor(getTextColor());
        drawRightJustified(g2d, getRowHeader(), 0, getRowHeaderWidth() - TEXT_MARGIN);
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
        drawLeftJustified(g2d, getRowFooter(),
                timeToColumnStart(24 * 60) + TEXT_MARGIN);
    }
}
