package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;

abstract class GridPainter {
    private static final Color MINOR_COLOR = new Color(0, 0, 0, 10);
    private static final Color MAJOR_COLOR = new Color(0, 0, 0, 40);

    private GridPainter() {

    }

    public static void paintGrid(Graphics2D g2d,
                                 JPanel target,
                                 AbstractRenderer renderer) {
        val startX = SwingUtilities.convertPoint(renderer, 0, 0, target).x;

        paintMajorLines(g2d, renderer, startX, target.getHeight());
        paintMinorLines(g2d, renderer, startX, target.getHeight());
    }

    private static void paintMajorLines(Graphics2D g2d,
                                        AbstractRenderer renderer,
                                        int startX, int height) {
        g2d.setColor(MAJOR_COLOR);
        for (int hour = 0; hour < 24; ++hour) {
            paintLine(g2d, renderer, startX, height, hour * 60);
        }
    }

    private static void paintMinorLines(Graphics2D g2d,
                                        AbstractRenderer renderer,
                                        int startX, int height) {
        g2d.setColor(MINOR_COLOR);
        for(int hour = 0; hour < 23; ++hour) {
            int minutes = hour * 60;
            paintLine(g2d, renderer, startX, height, minutes + 15);
            paintLine(g2d, renderer, startX, height, minutes + 30);
            paintLine(g2d, renderer, startX, height, minutes + 45);
        }
    }

    private static void paintLine(Graphics2D g2d,
                                  AbstractRenderer renderer,
                                  int startX, int height, int minutes) {
        final int x = renderer.timeToColumnCenter(minutes) + startX;
        g2d.drawLine(x, 0, x, height);
    }

}
