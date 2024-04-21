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
        g2d.setColor(getTextColor());

        drawRightJustified(getZoneIdString(), 0,
                getRowHeaderWidth() - TEXT_MARGIN,
                g2d);

        val hourColumnWidth = getHourColumnWidth();

        val offset = getUtcOffset();

        for (int hourUtc = 0; hourUtc < 24; ++hourUtc) {
            val hourStr = Integer.toString(normalizeHour(hourUtc + offset));
            val x = timeToColumnStart(hourUtc * 60);
            drawCentered(hourStr, x, hourColumnWidth, g2d);
        }

        drawLeftJustified(getLocationDisplayString(),
                timeToColumnStart(24 * 60) + TEXT_MARGIN,
                g2d);
    }

    @Override
    protected int getRendererRightLocation() {
        return getWidth() - getRowFooterWidth();
    }
}
