package org.rothe.john.working_hours.ui.canvas.painters;

import java.awt.*;

public final class CrossHatch {
    private static final int SPACING = 5;

    private CrossHatch() {

    }

    public static void draw(Graphics2D g2, Shape shape) {
        diagonal(shape, true).draw(g2);
        diagonal(shape, false).draw(g2);
    }

    private static Diagonals diagonal(Shape shape, boolean drawDown) {
        final Rectangle bounds = new Rectangle(shape.getBounds());
        bounds.grow(bounds.height, 0);

        final int xOffset = (int) Math.round(bounds.height / Math.sin(Math.PI / 2));
        final int topY = bounds.y;
        final int bottomY = bounds.y + bounds.height;

        if (drawDown) {
            return new Diagonals(bounds.x, bounds.x + bounds.width, xOffset, topY, bottomY);
        }
        return new Diagonals(bounds.x, bounds.x + bounds.width, xOffset, bottomY, topY);
    }

    private record Diagonals(int startX, int endX, int xOffset, int startY, int endY) {
        public void draw(Graphics2D g2) {
            for (int x = startX(); x < endX(); x += SPACING) {
                g2.drawLine(x, startY(), x + xOffset(), endY());
            }
        }
    }
}
