package org.rothe.john.working_hours.ui.canvas.painters;

import lombok.val;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.collaboration.CollabCalculator;
import org.rothe.john.working_hours.ui.canvas.st.Boundaries;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.util.Objects.isNull;

public class CollabZonePainter {
    private final Canvas canvas;
    private final SpaceTime spaceTime;
    private CollabCalculator calculator;

    public CollabZonePainter(Canvas canvas, SpaceTime spaceTime) {
        this.canvas = canvas;
        this.spaceTime = spaceTime;
        this.calculator = null;
    }

    public void initialize() {
        if (isNull(canvas.getTeam())) {
            this.calculator = null;
        } else {
            this.calculator = CollabCalculator.of(canvas.getTeam().getMembers());
        }
    }

    public void paintUnder(Graphics2D g2d, JPanel exampleRow) {
        val target = new PaintTarget(spaceTime, g2d, toRowStartX(exampleRow));
        val canvasHeight = canvas.getHeight();

        calculator.largest().forEach(shift -> target.fill(shift.time(), canvasHeight));
        calculator.shiftChanges().forEach(change -> target.draw(change.time(), canvasHeight));
    }

    public void paintOver(Graphics2D g2d, JPanel exampleRow) {
        val target = new PaintTarget(spaceTime, g2d, toRowStartX(exampleRow));

        calculator.largest().forEach(shift -> target.draw(shift.time(), canvas.getHeight()));
    }

    private int toRowStartX(JPanel exampleRow) {
        return SwingUtilities.convertPoint(exampleRow, 0, 0, canvas).x;
    }

    private static class PaintTarget {
        private static final Color LINE_COLOR = new Color(0, 0, 255, 40);
        private static final Color FILL_COLOR = new Color(0, 0, 255, 20);

        private final Graphics2D g2d;
        private final int startX;
        private final SpaceTime spaceTime;

        public PaintTarget(SpaceTime spaceTime, Graphics2D g2d, int startX) {
            this.g2d = g2d;
            this.startX = startX;
            this.spaceTime = spaceTime;
        }

        void draw(Time time, int height) {
            val x = startX + spaceTime.toColumnCenter(time);
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
            return spaceTime.toCenterBoundaries(pair);
        }
    }
}
