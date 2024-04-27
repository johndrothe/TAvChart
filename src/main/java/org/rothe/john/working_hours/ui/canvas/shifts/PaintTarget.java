package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;

import java.awt.Color;
import java.awt.Graphics2D;

@RequiredArgsConstructor
class PaintTarget {
    private final CanvasInfo canvasInfo;
    private final Graphics2D g2d;
    private final int startX;

    private static final Color LINE_COLOR = new Color(0, 0, 255, 40);
    public static final Color FILL_COLOR = new Color(0, 0, 255, 20);

    void drawLine(int minutesUtc, int height) {
        val x = minutesToLocation(minutesUtc);

        g2d.setColor(LINE_COLOR);
        g2d.drawLine(x, 0, x, height);
    }

    void fillShift(int startMinutesUtc, int endMinutesUtc, int height) {
        val x1 = minutesToLocation(startMinutesUtc);
        val x2 = minutesToLocation(endMinutesUtc);
        val width = x2 - x1;

        g2d.setColor(FILL_COLOR);
        g2d.fillRect(x1, 0, width, height);
    }

    void drawShift(int startMinutesUtc, int endMinutesUtc, int height) {
        g2d.setColor(LINE_COLOR);
        drawLine(startMinutesUtc, height);
        drawLine(endMinutesUtc, height);
    }

    private int minutesToLocation(int minutesUtc) {
        return canvasInfo.timeToColumnCenter(minutesUtc) + startX;
    }
}
