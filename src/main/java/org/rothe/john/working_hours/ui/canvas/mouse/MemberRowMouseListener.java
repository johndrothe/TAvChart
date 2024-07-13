package org.rothe.john.working_hours.ui.canvas.mouse;


import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.canvas.rows.MemberRow;
import org.rothe.john.working_hours.ui.canvas.util.CanvasCalculator;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import static java.util.Objects.nonNull;

public class MemberRowMouseListener extends CanvasMouseListener {
    private final Team team;
    private final MemberRow row;
    private Point dragStart = null;

    private MemberRowMouseListener(Team team, MemberRow row, CanvasCalculator calculator) {
        super(calculator);
        this.row = row;
        this.team = team;
    }

    public static void register(Team team, MemberRow row, CanvasCalculator calculator) {
        row.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        new MemberRowMouseListener(team, row, calculator).register(row);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        fireTeamUpdate(getDraggedHours(e.getX()));
        clear();
    }

    private void fireTeamUpdate(int draggedHours) {
        if (draggedHours != 0) {
            fireTeamChanged(team.withUpdatedMember(row.getMember(), updatedMember(draggedHours)));
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

    private void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, "Edit Normal Hours", newTeam);
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
