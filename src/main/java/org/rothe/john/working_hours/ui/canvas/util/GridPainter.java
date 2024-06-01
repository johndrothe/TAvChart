package org.rothe.john.working_hours.ui.canvas.util;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;

import javax.swing.*;
import java.awt.*;

public class GridPainter {
    private static final Color MINOR_COLOR = new Color(0, 0, 0, 10);
    private static final Color MAJOR_COLOR = new Color(0, 0, 0, 40);

    private final JPanel canvas;
    private final SpaceTime spaceTime;

    public GridPainter(JPanel canvas, SpaceTime spaceTime) {
        this.canvas = canvas;
        this.spaceTime = spaceTime;
    }

    public void paintGrid(Graphics2D g2d, JPanel exampleRow) {
        val target = new Target(spaceTime, g2d, toRowStartX(exampleRow));

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

    private record Target(SpaceTime spaceTime, Graphics2D g2d, int startX) {
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
            return spaceTime.toColumnCenter(minutesUtc) + startX;
        }
    }
}
