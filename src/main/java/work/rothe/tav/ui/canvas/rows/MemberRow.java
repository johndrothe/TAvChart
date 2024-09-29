package work.rothe.tav.ui.canvas.rows;

import lombok.Getter;
import lombok.val;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.ui.canvas.mouse.MemberRowMouseListener;
import work.rothe.tav.ui.canvas.painters.Stippled;
import work.rothe.tav.ui.canvas.util.Boundaries;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.canvas.util.Palette;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.List;

@Getter
public class MemberRow extends AbstractZoneRow {
    private final Document document;
    private final Member member;
    private int dragOffsetHours;

    public MemberRow(Document document, Member member, CanvasCalculator calculator, Palette palette) {
        super(calculator, member.zone(), palette);
        setOpaque(false);
        this.document = document;
        this.member = member;

        MemberRowMouseListener.register(document, this, calculator);
    }

    public void setDragOffsetHours(int hours) {
        this.dragOffsetHours = hours;
        repaint();
    }

    @Override
    public Dimension getMinimumSize() {
        return max(super.getMinimumSize(), doubleHeight(labelSize()));
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
        normalBoundaries.stream()
                .max(Comparator.comparing(Boundaries::width))
                .ifPresent(b -> drawText(g2d, b));
    }

    private void drawText(Graphics2D g2d, Boundaries boundaries) {
        g2d.setColor(getTextColor());
        val halfHeight = getHeight() / 2.0;
        drawCentered(g2d, getTitle(), boundaries.left(), 0.0, boundaries.width(), halfHeight);

        val g = subtitleGraphics(g2d);
        try {
            drawCentered(g, getSubtitle(), boundaries.left(), halfHeight, boundaries.width(), halfHeight);
        } finally {
            g.dispose();
        }
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

    private void drawLunch(Graphics2D g, Shape shape) {
        // don't adjust the clip and repaint unless it is in the dirty region
        if (!g.getClip().intersects(shape.getBounds2D())) {
            return;
        }
        Stippled.draw(g, shape, transparent(getLineColor()));
    }

    private List<Boundaries> normalBoundaries() {
        return getCalculator().toCenterBoundaries(member.normal().addHours(dragOffsetHours));
    }

    private List<Boundaries> lunchBoundaries() {
        return getCalculator().toCenterBoundaries(member.lunch().addHours(dragOffsetHours));
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

    private String getTitle() {
        return member.name();
    }

    private String getSubtitle() {
        return String.format("(%s / %s)", member.role(), member.location());
    }

    private Graphics2D subtitleGraphics(Graphics2D g2d) {
        Graphics2D g = (Graphics2D) g2d.create();
        g.setFont(g.getFont().deriveFont(Font.ITALIC));
        g.setColor(transparent(g.getColor()));
        return g;
    }

    private Color transparent(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 164);
    }

}
