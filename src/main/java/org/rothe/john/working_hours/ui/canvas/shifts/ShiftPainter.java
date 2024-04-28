package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;

import static java.util.Objects.isNull;

public class ShiftPainter {
    private final Canvas canvas;
    private final CanvasInfo canvasInfo;

    private ShiftTable table;

    public ShiftPainter(Canvas canvas, CanvasInfo canvasInfo) {
        this.canvas = canvas;
        this.canvasInfo = canvasInfo;
        this.table = null;
    }

    public void initialize() {
        if (isNull(canvas.getTeam())) {
            this.table = null;
        } else {
            this.table = ShiftTable.of(canvas.getTeam().getMembers());
        }
    }

    public void paintUnder(Graphics2D g2d, JPanel exampleRow) {
        val target = new PaintTarget(canvasInfo, g2d, toRowStartX(exampleRow));
        val canvasHeight = canvas.getHeight();

        table.getLargestShifts()
                .forEach(s -> target.fillShift(s.start(), s.end(), canvasHeight));

        table.getShiftChanges()
                .forEach(s -> target.drawLine(s, canvasHeight));
    }

    public void paintOver(Graphics2D g2d, JPanel exampleRow) {
        val target = new PaintTarget(canvasInfo, g2d, toRowStartX(exampleRow));

        table.getLargestShifts()
                .forEach(s -> target.drawShift(s.start(), s.end(), canvas.getHeight()));
    }

    private int toRowStartX(JPanel exampleRow) {
        return SwingUtilities.convertPoint(exampleRow, 0, 0, canvas).x;
    }
}
