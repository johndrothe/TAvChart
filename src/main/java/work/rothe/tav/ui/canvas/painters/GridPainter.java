package work.rothe.tav.ui.canvas.painters;

import lombok.val;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;

import javax.swing.*;
import java.awt.*;

public class GridPainter {
    private static final Color MINOR_COLOR = new Color(0, 0, 0, 10);
    private static final Color MAJOR_COLOR = new Color(0, 0, 0, 40);

    private final JPanel canvas;
    private final CanvasCalculator canvasCalculator;

    public GridPainter(JPanel canvas, CanvasCalculator canvasCalculator) {
        this.canvas = canvas;
        this.canvasCalculator = canvasCalculator;
    }

    public void paintGrid(Graphics2D g2d, JPanel exampleRow) {
        val target = new Target(canvasCalculator, g2d, toRowStartX(exampleRow));

        paintMajorLines(target);
        paintMinorLines(target);
    }

    private void paintMajorLines(Target target) {
        target.switchToMajorColor();
        int borderHour = canvasCalculator.getBorderHour();
        for (int hour = borderHour; hour < borderHour + 25; ++hour) {
            paintLine(target, hour * 60);
        }
    }

    private void paintMinorLines(Target target) {
        target.switchToMinorColor();
        int borderHour = canvasCalculator.getBorderHour();
        for (int hour = borderHour; hour < borderHour + 24; ++hour) {
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

    private record Target(CanvasCalculator canvasCalculator, Graphics2D g2d, int startX) {
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
            return canvasCalculator.toColumnCenter(minutesUtc) + startX;
        }
    }
}
