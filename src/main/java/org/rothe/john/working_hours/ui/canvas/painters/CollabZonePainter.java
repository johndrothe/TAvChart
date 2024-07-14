package org.rothe.john.working_hours.ui.canvas.painters;

import lombok.val;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.TimePair;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.canvas.util.Boundaries;
import org.rothe.john.working_hours.ui.canvas.util.CanvasCalculator;
import org.rothe.john.working_hours.ui.collaboration.CollabCalculator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import static java.util.Objects.isNull;

public class CollabZonePainter {
    private final Canvas canvas;
    private final CanvasCalculator canvasCalculator;
    private CollabCalculator collabCalculator;

    public CollabZonePainter(Canvas canvas, CanvasCalculator canvasCalculator) {
        this.canvas = canvas;
        this.canvasCalculator = canvasCalculator;
        this.collabCalculator = null;
    }

    public void initialize() {
        if (isNull(canvas.getTeam())) {
            this.collabCalculator = null;
        } else {
            this.collabCalculator = CollabCalculator.of(canvas.getTeam().getMembers());
        }
    }

    public void paintUnder(Graphics2D g2d, JPanel exampleRow) {
        val target = newTarget(g2d, toRowStartX(exampleRow));

        collabCalculator.largest().forEach(shift -> target.fill(shift.time(), canvas.getHeight()));
        collabCalculator.shiftChanges().forEach(change -> target.draw(change.time(), canvas.getHeight()));
    }

    public void paintOver(Graphics2D g2d, JPanel exampleRow) {
        val target = newTarget(g2d, toRowStartX(exampleRow));

        collabCalculator.largest().forEach(shift -> target.draw(shift.time(), canvas.getHeight()));
    }

    private int toRowStartX(JPanel exampleRow) {
        return SwingUtilities.convertPoint(exampleRow, 0, 0, canvas).x;
    }

    private PaintTarget newTarget(Graphics2D g2d, int startX) {
        return new PaintTarget(canvasCalculator, g2d, startX);
    }

    private record PaintTarget(CanvasCalculator calculator, Graphics2D g2d, int startX) {
        private static final Color LINE_COLOR = new Color(0, 0, 255, 40);
        private static final Color FILL_COLOR = new Color(0, 0, 255, 20);

        void draw(Time time, int height) {
            val x = startX + calculator.toColumnCenter(time);
            g2d.setColor(LINE_COLOR);
            g2d.drawLine(x, 0, x, height);
        }

        void fill(TimePair pair, int height) {
            toBoundaries(pair).forEach(b -> fill(b, height));
        }

        void draw(TimePair pair, int height) {
            toBoundaries(pair).forEach(b -> draw(b, height));
        }

        private void fill(Boundaries boundaries, int height) {
            g2d.setColor(FILL_COLOR);
            g2d.fillRect(startX + boundaries.left(), 0, boundaries.width(), height);
        }

        private void draw(Boundaries boundaries, int height) {
            g2d.setColor(LINE_COLOR);
            g2d.drawLine(startX + boundaries.left(), 0, startX + boundaries.left(), height);
            g2d.drawLine(startX + boundaries.right(), 0, startX + boundaries.right(), height);
        }

        private List<Boundaries> toBoundaries(TimePair pair) {
            return calculator.toCenterBoundaries(pair);
        }
    }
}
