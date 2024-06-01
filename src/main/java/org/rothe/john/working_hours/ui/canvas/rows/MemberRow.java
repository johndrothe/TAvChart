package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.Getter;
import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.st.Boundaries;
import org.rothe.john.working_hours.ui.canvas.util.Palette;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.List;

@Getter
public class MemberRow extends AbstractZoneRow {
    private static final Color LUNCH_FILL = new Color(230, 230, 230);
    private static final Color LUNCH_LINE = new Color(210, 210, 210);

    private final Member member;

    public MemberRow(CanvasInfo canvasInfo, Member member, Palette palette) {
        super(canvasInfo, member.zone(), palette);
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
        lunch.forEach(s -> fill(g2d, s, LUNCH_FILL));
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

    private List<Boundaries> normalBoundaries() {
        return getSpaceTime().toCenterBoundaries(member.normal());
    }
    private List<Boundaries> lunchBoundaries() {
        return getSpaceTime().toCenterBoundaries(member.lunch());
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
}
