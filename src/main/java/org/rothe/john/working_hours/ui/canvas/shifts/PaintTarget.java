package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.val;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.st.Boundaries;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

import java.awt.*;
import java.util.List;

class PaintTarget {
    private static final Color LINE_COLOR = new Color(0, 0, 255, 40);
    private static final Color FILL_COLOR = new Color(0, 0, 255, 20);

    private final Graphics2D g2d;
    private final int startX;
    private final SpaceTime spaceTime;

    public PaintTarget(CanvasInfo canvasInfo, Graphics2D g2d, int startX) {
        this.g2d = g2d;
        this.startX = startX;
        this.spaceTime = SpaceTime.from(canvasInfo);
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
