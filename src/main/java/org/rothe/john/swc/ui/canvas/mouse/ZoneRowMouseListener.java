package org.rothe.john.swc.ui.canvas.mouse;


import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.canvas.rows.ZoneRow;
import org.rothe.john.swc.ui.canvas.util.CanvasCalculator;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import static java.util.Objects.nonNull;

public class ZoneRowMouseListener extends CanvasMouseListener {
    private final Document document;
    private Point dragStart = null;

    private ZoneRowMouseListener(Document document, CanvasCalculator calculator) {
        super(calculator);
        this.document = document;
    }

    public static void register(Document document, ZoneRow row, CanvasCalculator calculator) {
        row.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        new ZoneRowMouseListener(document, calculator).register(row);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        fireDocumentChanged(document.withBorderHour(calculator().getBorderHour()));
        clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging()) {
            calculator().setBorderHourOffset(-calculateBorderHourOffset(e.getX()));
        }
    }

    private void fireDocumentChanged(Document newDocument) {
        Documents.fireDocumentChanged(this, "Edit Border Hour", newDocument);
    }

    private int calculateBorderHourOffset(int xLocation) {
        return (int) ((xLocation - dragStart.x) / calculator().hourColumnWidth());
    }

    private boolean isDragging() {
        return nonNull(dragStart);
    }

    private void clear() {
        dragStart = null;
    }
}
