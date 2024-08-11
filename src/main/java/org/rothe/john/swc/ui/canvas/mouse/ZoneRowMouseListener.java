package org.rothe.john.swc.ui.canvas.mouse;


import org.rothe.john.swc.ui.canvas.rows.ZoneRow;
import org.rothe.john.swc.ui.canvas.util.CanvasCalculator;

import java.awt.*;
import java.awt.event.MouseEvent;

import static java.util.Objects.nonNull;

public class ZoneRowMouseListener extends CanvasMouseListener {
    private int dragHour = -1;
    private Point dragStart = null;

    private ZoneRowMouseListener(CanvasCalculator calculator) {
        super(calculator);
    }

    public static void register(ZoneRow row, CanvasCalculator calculator) {
        row.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        new ZoneRowMouseListener(calculator).register(row);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        dragStart = e.getPoint();
        dragHour = calculator().getBorderHour();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (isDragging()) {
            calculator().setBorderHour(calculateNewBorderHour(e.getX()));
        }
    }

    private int calculateNewBorderHour(int xLocation) {
        return dragHour - (int) ((xLocation - dragStart.x) / calculator().hourColumnWidth());
    }

    private boolean isDragging() {
        return nonNull(dragStart);
    }

    private void clear() {
        dragHour = -1;
        dragStart = null;
    }
}
