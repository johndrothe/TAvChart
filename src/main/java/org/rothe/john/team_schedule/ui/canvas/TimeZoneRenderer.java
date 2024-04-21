package org.rothe.john.team_schedule.ui.canvas;

import org.rothe.john.team_schedule.util.Palette;
import lombok.val;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.ZoneId;

public class TimeZoneRenderer extends ZonedRenderer {
    private static final int TEXT_MARGIN = 5;

    public TimeZoneRenderer(ZoneId zoneId, Palette palette) {
        super(zoneId, palette);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;
        val lineHintColor = getLineHintColor();
        val hourColumnWidth = getHourColumnWidth();
        val offset = getUtcOffset();

        g2d.setColor(getTextColor());
        drawRightJustified(g2d, getZoneIdString(), 0, getRowHeaderWidth() - TEXT_MARGIN);

        for (int hourUtc = 0; hourUtc < 24; ++hourUtc) {
            val x = timeToColumnStart(hourUtc * 60);

            g2d.setColor(lineHintColor);
            g2d.drawLine(x, 6, x, getHeight() - 6);

            g2d.setColor(getTextColor());
            val hourStr = Integer.toString(normalizeHour(hourUtc + offset));
            drawCentered(g2d, hourStr, x, hourColumnWidth);
        }

        drawLeftJustified(g2d, getLocationDisplayString(),
                timeToColumnStart(24 * 60) + TEXT_MARGIN);
    }

    @Override
    protected int getRendererRightLocation() {
        return getWidth() - getRowFooterWidth();
    }
}
