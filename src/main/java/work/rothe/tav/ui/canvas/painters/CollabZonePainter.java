package work.rothe.tav.ui.canvas.painters;

import lombok.val;
import work.rothe.tav.model.Time;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.canvas.util.Boundaries;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.collaboration.CollabCalculator;
import work.rothe.tav.ui.collaboration.CollabZone;
import work.rothe.tav.ui.collaboration.ShiftChange;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class CollabZonePainter {
    private static final Color LINE_COLOR = new Color(0, 0, 255, 40);
    private static final Color FILL_COLOR = new Color(0, 0, 255, 20);

    private final Canvas canvas;
    private final CanvasCalculator canvasCalculator;
    private CollabCalculator collabCalculator;

    public CollabZonePainter(Canvas canvas, CanvasCalculator canvasCalculator) {
        this.canvas = canvas;
        this.canvasCalculator = canvasCalculator;
        this.collabCalculator = null;
    }

    public void initialize() {
        if (isNull(canvas.getDocument())) {
            this.collabCalculator = null;
        } else {
            this.collabCalculator = CollabCalculator.of(canvas.getDocument().members());
        }
    }

    public void paintUnder(Graphics2D g2d, JPanel exampleRow) {
        val target = newTarget(g2d, toRowStartX(exampleRow));

        collabZoneTimeBoundaries().forEach(target::fill);
        shiftChangeTimes().forEach(target::draw);
    }

    public void paintOver(Graphics2D g2d, JPanel exampleRow) {
        val target = newTarget(g2d, toRowStartX(exampleRow));

        collabZoneTimeBoundaries().forEach(target::draw);
    }

    private Stream<Time> shiftChangeTimes() {
        return collabCalculator.shiftChanges().stream()
                .map(ShiftChange::time);
    }

    private List<Boundaries> collabZoneTimeBoundaries() {
        return collabCalculator.largest()
                .map(CollabZone::time)
                .map(canvasCalculator::toCenterBoundaries)
                .orElse(List.of());
    }

    private int toRowStartX(JPanel exampleRow) {
        return SwingUtilities.convertPoint(exampleRow, 0, 0, canvas).x;
    }

    private PaintTarget newTarget(Graphics2D g2d, int startX) {
        return new PaintTarget(canvasCalculator, g2d, startX, canvas.getHeight());
    }

    private record PaintTarget(CanvasCalculator calculator,
                               Graphics2D g2d,
                               int startX,
                               int canvasHeight) {
        void draw(Time time) {
            val x = startX + calculator.toColumnCenter(time);
            g2d.setColor(LINE_COLOR);
            g2d.drawLine(x, 0, x, canvasHeight);
        }

        private void fill(Boundaries boundaries) {
            g2d.setColor(FILL_COLOR);
            g2d.fillRect(startX + boundaries.left(), 0, boundaries.width(), canvasHeight);
        }

        private void draw(Boundaries boundaries) {
            g2d.setColor(LINE_COLOR);
            g2d.drawLine(startX + boundaries.left(), 0, startX + boundaries.left(), canvasHeight);
            g2d.drawLine(startX + boundaries.right(), 0, startX + boundaries.right(), canvasHeight);
        }
    }
}
