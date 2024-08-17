package org.rothe.john.swc.ui.canvas.rows;

import lombok.val;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.model.Time;
import org.rothe.john.swc.model.Zone;
import org.rothe.john.swc.ui.canvas.mouse.ZoneRowMouseListener;
import org.rothe.john.swc.ui.canvas.util.CanvasCalculator;
import org.rothe.john.swc.ui.canvas.util.Palette;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ZoneRow extends AbstractZoneRow {
    private static final int TEXT_MARGIN = 5;
    private final Color lineHintColor;

    public ZoneRow(Document document, CanvasCalculator calculator, Zone zone, Palette palette) {
        super(calculator, zone, palette);
        lineHintColor = getLineHintColor();

        ZoneRowMouseListener.register(document, this, calculator);
    }

    @Override
    protected int getRowMaxX() {
        return getWidth() - getRowFooterWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;
        val hourColumnWidth = getCalculator().hourColumnWidth();
        val offset = getOffsetHours();

        paintRowShape(g);

        paintRowHeader(g2d);

        int borderHour = borderHour();

        for (int hourUtc = borderHour; hourUtc < borderHour + 25; ++hourUtc) {
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
        val x = getCalculator().toColumnStart(hourUtc * 60);

        g2d.setColor(lineHintColor);
        g2d.drawLine(x, 6, x, getHeight() - 6);

        g2d.setColor(getTextColor());
        val hourStr = Integer.toString(Time.normalizeHour(hourUtc + offset));
        drawCentered(g2d, hourStr, x, hourColumnWidth);
    }

    private void paintRowFooter(Graphics2D g2d) {
        val x = getCalculator().toColumnStart((borderHour() +25) * 60) + TEXT_MARGIN;
        g2d.setColor(getTextColor());
        drawLeftJustified(g2d, getRowFooter(), x);
    }

    private int borderHour() {
        return getCalculator().getBorderHour();
    }

    private Color getLineHintColor() {
        return new Color(getLineColor().getRed(), getLineColor().getGreen(), getLineColor().getBlue(), 40);
    }
}
