package org.rothe.john.working_hours.ui.canvas.rows;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.st.Boundaries;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;
import org.rothe.john.working_hours.ui.canvas.util.Palette;
import lombok.Getter;
import lombok.val;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.List;

@Getter
public class MemberRow extends AbstractZoneRow {
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

        val boundaries = getBoundaries();

        paintRowShapes(g2d, toShapes(boundaries));

        // TODO: render the text in pieces across the schedule, if necessary
        boundaries.stream()
                .max(Comparator.comparing(Boundaries::width))
                .ifPresent(b -> drawRowText(g2d, b));
    }

    private void drawRowText(Graphics2D g2d, Boundaries boundaries) {
        g2d.setColor(getTextColor());
        drawCentered(g2d, getDisplayString(), boundaries.left(), boundaries.width());
    }

    private void paintRowShapes(Graphics2D g2d, List<Shape> shapes) {
        shapes.forEach(s -> paintRowShape(g2d, s));
    }

    private void paintRowShape(Graphics2D g, Shape s) {
        g.setColor(getFillColor());
        g.fill(s);
        g.setColor(getLineColor());
        g.draw(s);
    }

    private List<Boundaries> getBoundaries() {
        return getSpaceTime().toCenterBoundaries(normalPair());
    }

    private List<Shape> toShapes(List<Boundaries> boundaries) {
        return boundaries.stream().map(this::toShape).toList();
    }

    private TimePair normalPair() {
        return new TimePair(
                member.availability().normalStart(),
                member.availability().normalEnd());
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
        return timeToColumnCenter(member.availability().normalStart());
    }

    @Override
    protected int getRowMaxX() {
        return timeToColumnCenter(member.availability().normalEnd());
    }

    private String getDisplayString() {
        return String.format("%s (%s / %s)", member.name(), member.role(), member.location());
    }
}
