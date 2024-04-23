package org.rothe.john.working_hours.ui.canvas;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.rows.CanvasRow;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;

class GridPainter {
    private static final Color MINOR_COLOR = new Color(0, 0, 0, 10);
    private static final Color MAJOR_COLOR = new Color(0, 0, 0, 40);

    private final JPanel canvas;

    GridPainter(JPanel canvas) {
        this.canvas = canvas;
    }

    public void paintGrid(Graphics2D g2d, CanvasRow firstRow) {
        val target = new Target(g2d, firstRow, toRowStartX(firstRow));

        paintMajorLines(target);
        paintMinorLines(target);
    }

    private void paintMajorLines(Target target) {
        target.switchToMajorColor();
        for (int hour = 0; hour < 24; ++hour) {
            paintLine(target, hour * 60);
        }
    }

    private void paintMinorLines(Target target) {
        target.switchToMinorColor();
        for (int hour = 0; hour < 23; ++hour) {
            int minutes = hour * 60;
            paintLine(target, minutes + 15);
            paintLine(target, minutes + 30);
            paintLine(target, minutes + 45);
        }
    }

    private void paintLine(Target target, int minutes) {
        target.drawLine(minutes, canvas.getHeight());
    }

    private int toRowStartX(CanvasRow row) {
        return SwingUtilities.convertPoint(row, 0, 0, canvas).x;
    }

    private record Target(Graphics2D g2d, CanvasRow firstRow, int startX) {
        void switchToMinorColor() {
            g2d.setColor(MINOR_COLOR);
        }

        void switchToMajorColor() {
            g2d.setColor(MAJOR_COLOR);
        }

        void drawLine(int minutes, int height) {
            val x = minutesToLocation(minutes);
            g2d.drawLine(x, 0, x, height);
        }

        private int minutesToLocation(int minutes) {
            return firstRow.timeToColumnCenter(minutes) + startX;
        }
    }
}
