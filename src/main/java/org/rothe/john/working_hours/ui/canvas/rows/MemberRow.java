package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.Getter;
import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.ui.canvas.util.Boundaries;
import org.rothe.john.working_hours.ui.canvas.util.CanvasCalculator;
import org.rothe.john.working_hours.ui.canvas.util.Palette;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.List;

@Getter
public class MemberRow extends AbstractZoneRow {
    private static final Color LUNCH_LINE = new Color(255, 0, 255, 25);
    private final Member member;

    public MemberRow(CanvasCalculator calculator, Member member, Palette palette) {
        super(calculator, member.zone(), palette);
        setOpaque(false);
        this.member = member;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;

        val normalBoundaries = normalBoundaries();

        paintShapes(g2d, toShapes(normalBoundaries), toShapes(lunchBoundaries()));

        drawText(g2d, normalBoundaries);
    }

    private void drawText(Graphics2D g2d, List<Boundaries> normalBoundaries) {
        // TODO: render the text in pieces across the schedule, if necessary
        normalBoundaries.stream()
                .max(Comparator.comparing(Boundaries::width))
                .ifPresent(b -> drawText(g2d, b));
    }

    private void drawText(Graphics2D g2d, Boundaries boundaries) {
        g2d.setColor(getTextColor());
        drawCentered(g2d, getDisplayString(), boundaries.left(), boundaries.width());
    }

    private void paintShapes(Graphics2D g2d, List<Shape> normal, List<Shape> lunch) {
        normal.forEach(s -> fill(g2d, s, getFillColor()));
        lunch.forEach(s -> drawLunch(g2d, s));
        normal.forEach(s -> draw(g2d, s, getLineColor()));
    }

    private void fill(Graphics2D g, Shape s, Color fill) {
        g.setColor(fill);
        g.fill(s);
    }

    private void draw(Graphics2D g, Shape s, Color line) {
        g.setColor(line);
        g.draw(s);
    }

    private void drawLunch(Graphics2D g, Shape s) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(LUNCH_LINE);
        g2.draw(s);
        g2.setClip(s);

        drawDiagonals(g2, toDiagonals(s.getBounds(), true));
        drawDiagonals(g2, toDiagonals(s.getBounds(), false));

        g2.dispose();
    }

    private void drawDiagonals(Graphics2D g2, Diagonals diagonals) {
        final int DIAGONAL_SPACING = 5;
        for (int x = diagonals.startX(); x < diagonals.endX(); x += DIAGONAL_SPACING) {
            g2.drawLine(x, diagonals.startY(), x + diagonals.xOffset(), diagonals.endY());
        }
    }

    private List<Boundaries> normalBoundaries() {
        return getCalculator().toCenterBoundaries(member.normal());
    }

    private List<Boundaries> lunchBoundaries() {
        return getCalculator().toCenterBoundaries(member.lunch());
    }

    private List<Shape> toShapes(List<Boundaries> boundaries) {
        return boundaries.stream().map(this::toShape).toList();
    }

    private Shape toShape(Boundaries boundaries) {
        double x = boundaries.left() + 1;
        double y = 1;
        double width = boundaries.width() - 2;
        double height = getHeight() - 2;
        double arc = getHeight() / 3.0;
        return new RoundRectangle2D.Double(x, y, width, height, arc, arc);
    }

    @Override
    protected int getRowMinX() {
        return timeToColumnCenter(member.normal().left());
    }

    @Override
    protected int getRowMaxX() {
        return timeToColumnCenter(member.normal().right());
    }

    private String getDisplayString() {
        return String.format("%s (%s / %s)", member.name(), member.role(), member.location());
    }

    private static Diagonals toDiagonals(Rectangle shapeBounds, boolean drawDown) {
        final Rectangle bounds = new Rectangle(shapeBounds);
        bounds.grow(bounds.height, 0);

        final int xOffset = (int) Math.round(bounds.height / Math.sin(Math.PI / 2));
        final int topY = bounds.y;
        final int bottomY = bounds.y + bounds.height;

        if (drawDown) {
            return new Diagonals(bounds.x, bounds.x + bounds.width, xOffset, topY, bottomY);
        }
        return new Diagonals(bounds.x, bounds.x + bounds.width, xOffset, bottomY, topY);
    }

    private record Diagonals(int startX, int endX, int xOffset, int startY, int endY) {

    }
}
