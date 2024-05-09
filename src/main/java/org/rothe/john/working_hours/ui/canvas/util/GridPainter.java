package org.rothe.john.working_hours.ui.canvas.util;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;

public class GridPainter {
    private static final Color MINOR_COLOR = new Color(0, 0, 0, 10);
    private static final Color MAJOR_COLOR = new Color(0, 0, 0, 40);

    private final JPanel canvas;
    private final CanvasInfo canvasInfo;

    public GridPainter(JPanel canvas, CanvasInfo canvasInfo) {
        this.canvas = canvas;
        this.canvasInfo = canvasInfo;
    }

    public void paintGrid(Graphics2D g2d, JPanel exampleRow) {
        val target = new Target(canvasInfo, g2d, toRowStartX(exampleRow));

        paintMajorLines(target);
        paintMinorLines(target);
    }

    private void paintMajorLines(Target target) {
        target.switchToMajorColor();
        for (int hour = 0; hour < 25; ++hour) {
            paintLine(target, hour * 60);
        }
    }

    private void paintMinorLines(Target target) {
        target.switchToMinorColor();
        for (int hour = 0; hour < 24; ++hour) {
            int minutesUtc = hour * 60;
            paintLine(target, minutesUtc + 15);
            paintLine(target, minutesUtc + 30);
            paintLine(target, minutesUtc + 45);
        }
    }

    private void paintLine(Target target, int minutesUtc) {
        target.drawLine(minutesUtc, canvas.getHeight());
    }

    private int toRowStartX(JPanel exampleRow) {
        return SwingUtilities.convertPoint(exampleRow, 0, 0, canvas).x;
    }

    private record Target(CanvasInfo canvasInfo, Graphics2D g2d, int startX) {
        void switchToMinorColor() {
            g2d.setColor(MINOR_COLOR);
        }

        void switchToMajorColor() {
            g2d.setColor(MAJOR_COLOR);
        }

        void drawLine(int minutesUtc, int height) {
            val x = minutesToLocation(minutesUtc);
            g2d.drawLine(x, 0, x, height);
        }

        private int minutesToLocation(int minutesUtc) {
            return canvasInfo.timeToColumnCenter(minutesUtc) + startX;
        }
    }
}
