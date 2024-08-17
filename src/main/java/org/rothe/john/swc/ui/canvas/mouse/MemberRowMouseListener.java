package org.rothe.john.swc.ui.canvas.mouse;


import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.ui.canvas.rows.MemberRow;
import org.rothe.john.swc.ui.canvas.util.CanvasCalculator;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import static java.util.Objects.nonNull;

public class MemberRowMouseListener extends CanvasMouseListener {
    private final Document document;
    private final MemberRow row;
    private Point dragStart = null;

    private MemberRowMouseListener(Document document, MemberRow row, CanvasCalculator calculator) {
        super(calculator);
        this.row = row;
        this.document = document;
    }

    public static void register(Document document, MemberRow row, CanvasCalculator calculator) {
        row.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        new MemberRowMouseListener(document, row, calculator).register(row);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        fireDocumentUpdate(getDraggedHours(e.getX()));
        clear();
    }

    private void fireDocumentUpdate(int draggedHours) {
        if (draggedHours != 0) {
            fireDocumentChanged(document.withUpdatedMember(row.getMember(), updatedMember(draggedHours)));
        }
    }

    private Member updatedMember(int draggedHours) {
        return row.getMember()
                .withNormal(row.getMember().normal().addHours(draggedHours))
                .withLunch(row.getMember().lunch().addHours(draggedHours));
    }

    private int getDraggedHours(int xLocation) {
        return (int) ((xLocation - dragStart.x) / calculator().hourColumnWidth());
    }

    private void fireDocumentChanged(Document newDocument) {
        Documents.fireDocumentChanged(this, "Edit Normal Hours", newDocument);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (isDragging()) {
            row.setDragOffsetHours(getDraggedHours(e.getX()));
        }
    }

    private boolean isDragging() {
        return nonNull(dragStart);
    }

    private void clear() {
        dragStart = null;
    }
}
