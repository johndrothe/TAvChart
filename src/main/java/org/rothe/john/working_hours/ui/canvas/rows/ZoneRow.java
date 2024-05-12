package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.val;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.util.Palette;
import org.rothe.john.working_hours.model.Zone;

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
    protected int getRowMaxX() {
        return getWidth() - getRowFooterWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;
        val hourColumnWidth = getCanvasInfo().getHourColumnWidth();
        val offset = getOffsetHours();

        paintRowShape(g);

        paintRowHeader(g2d);

        for (int hourUtc = 0; hourUtc < 25; ++hourUtc) {
            paintTimeZone(g2d, hourUtc, offset, hourColumnWidth);
        }

        paintRowFooter(g2d);
    }

    private void paintRowShape(Graphics g) {
        g.setColor(getFillColor());
        fillRowArea(g, getRowMinX(), getRowWidth(), getHeight());

        g.setColor(getLineColor());
        drawRowBorder(g, getRowMinX(), getRowWidth(), getHeight());
    }

    private void paintRowHeader(Graphics2D g2d) {
        g2d.setColor(getTextColor());
        drawRightJustified(g2d, getRowHeader(), 0, getRowHeaderWidth() - TEXT_MARGIN);
    }

    private void paintTimeZone(Graphics2D g2d, int hourUtc, int offset, double hourColumnWidth) {
        val x = getCanvasInfo().timeToColumnStart(hourUtc * 60);

        g2d.setColor(lineHintColor);
        g2d.drawLine(x, 6, x, getHeight() - 6);

        g2d.setColor(getTextColor());
        val hourStr = Integer.toString(Time.normalizeHour(hourUtc + offset));
        drawCentered(g2d, hourStr, x, hourColumnWidth);
    }

    private void paintRowFooter(Graphics2D g2d) {
        val x = getCanvasInfo().timeToColumnStart(25 * 60)+ TEXT_MARGIN;
        g2d.setColor(getTextColor());
        drawLeftJustified(g2d, getRowFooter(), x);
    }

    private Color getLineHintColor() {
        return new Color(getLineColor().getRed(), getLineColor().getGreen(), getLineColor().getBlue(), 40);
    }
}
