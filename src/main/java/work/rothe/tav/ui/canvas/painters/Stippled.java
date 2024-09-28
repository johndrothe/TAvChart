package work.rothe.tav.ui.canvas.painters;

import java.awt.*;

public final class Stippled {
    private static final int SPACING = 5;
    private static final BasicStroke DASHED_STROKE = newDashedStroke();

    private Stippled() {

    }

    public static void draw(Graphics2D g, Shape shape, Color color) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setColor(color);
            g2d.setStroke(DASHED_STROKE);

            Rectangle bounds = shape.getBounds();
            bounds.grow(-2, -2);
            g2d.setClip(shape);

            diagonal(shape).draw(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private static Diagonals diagonal(Shape shape) {
        final Rectangle bounds = new Rectangle(shape.getBounds());
        bounds.grow(bounds.height, 0);

        final int xOffset = (int) Math.round(bounds.height / Math.sin(Math.PI / 2));
        final int topY = bounds.y;
        final int bottomY = bounds.y + bounds.height;

        return new Diagonals(bounds.x, bounds.x + bounds.width, xOffset, topY, bottomY);
    }

    private record Diagonals(int startX, int endX, int xOffset, int startY, int endY) {
        public void draw(Graphics2D g2) {
            for (int x = startX(); x < endX(); x += SPACING) {
                g2.drawLine(x, startY(), x + xOffset(), endY());
            }
        }
    }

    private static BasicStroke newDashedStroke() {
        return new BasicStroke(1.0f,
                BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                1.0f, new float[]{1, 3}, 0f);
    }
}
