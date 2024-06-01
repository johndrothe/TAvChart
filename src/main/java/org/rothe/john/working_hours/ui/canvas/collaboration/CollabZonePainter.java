package org.rothe.john.working_hours.ui.canvas.collaboration;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.canvas.collaboration.calculator.CollabCalculator;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;

import javax.swing.*;
import java.awt.*;

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
}
