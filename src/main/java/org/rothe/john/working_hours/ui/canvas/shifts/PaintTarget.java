package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

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

    void fillShift(TimePair pair, int height) {
        val x1 = minutesToLocation(pair.left().totalMinutesInUtc());
        val x2 = minutesToLocation(pair.right().totalMinutesInUtc());
        val width = x2 - x1;

        g2d.setColor(FILL_COLOR);
        g2d.fillRect(x1, 0, width, height);
    }

    void drawShift(TimePair pair, int height) {
        g2d.setColor(LINE_COLOR);
        drawLine(pair.left().totalMinutesInUtc(), height);
        drawLine(pair.right().totalMinutesInUtc(), height);
    }

    private int minutesToLocation(int minutesUtc) {
        return canvasInfo.timeToColumnCenter(minutesUtc) + startX;
    }
}
