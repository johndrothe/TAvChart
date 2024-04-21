package org.rothe.john.team_schedule.ui.canvas;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractRenderer extends JPanel {
    private final Color textColor = Color.BLACK;
    private final Color lineColor;
    private final Color fillColor;

    @Setter(AccessLevel.PROTECTED)
    private int rowHeaderWidth;
    @Setter(AccessLevel.PROTECTED)
    private int rowFooterWidth;

    protected AbstractRenderer(Color fillColor, Color lineColor) {
        super(new BorderLayout());
        this.fillColor = fillColor;
        this.lineColor = lineColor;
        setFont(getFont().deriveFont(Font.PLAIN));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        applyRenderingHints((Graphics2D) g);

        g.setColor(fillColor);
        fillRenderer(g, getRendererLeftLocation(), 0, getRendererDrawWidth(), getHeight());

        g.setColor(lineColor);
        drawBorder(g, getRendererLeftLocation(), 0, getRendererDrawWidth(), getHeight());
    }

    protected void applyRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    protected int getRendererLeftLocation() {
        return 0;
    }

    protected int getRendererRightLocation() {
        return getWidth();
    }

    protected int getRendererDrawWidth() {
        return getRendererRightLocation() - getRendererLeftLocation();
    }

    private void fillRenderer(Graphics g, int x, int y, int width, int height) {
        val arc = height / 3;
        g.fillRoundRect(x + 1, y + 1, width - 2, height - 2, arc, arc);
    }

    private void drawBorder(Graphics g, int x, int y, int width, int height) {
        val arc = height / 3;
        g.drawRoundRect(x + 1, y + 1, width - 2, height - 2, arc, arc);
    }

    protected double getHourColumnWidth() {
        return (getWidth() - (getRowHeaderWidth() + getRowFooterWidth())) / 24.0;
    }

    protected void drawCentered(Graphics2D g2d, String text, double x, double width) {
        val bounds = getStrBounds(g2d, text);
        val dx = (float) (x + width / 2.0 - bounds.getWidth() / 2.0);
        val dy = (float) (getHeight() / 2.0 + bounds.getHeight() / 2.0);

        g2d.drawString(text, dx, dy);
    }

    protected void drawRightJustified(Graphics2D g2d, String text, double x, double width) {
        val bounds = getStrBounds(g2d, text);
        val dx = (float) (x + width - bounds.getWidth());
        val dy = (float) (getHeight() / 2.0 + bounds.getHeight() / 2.0);

        g2d.drawString(text, dx, dy);
    }

    protected void drawLeftJustified(Graphics2D g2d, String text, double x) {
        val bounds = getStrBounds(g2d, text);
        val dy = (float) (getHeight() / 2.0 + bounds.getHeight() / 2.0);

        g2d.drawString(text, (float)x, dy);
    }

    private Rectangle2D getStrBounds(Graphics2D g2d, String text) {
        return g2d.getFontMetrics().getStringBounds(text, g2d);
    }

    protected int timeToColumnStart(int minutesUtc) {
        return getRowHeaderWidth() + (int) Math.round(minutesUtc / 60.0 * getHourColumnWidth());
    }

    protected int timeToColumnCenter(int minutesUtc) {
        return getRowHeaderWidth() + (int) Math.round(minutesUtc / 60.0 * getHourColumnWidth() + getHourColumnWidth() / 2.0);
    }

    protected Color getLineHintColor() {
        return new Color(getLineColor().getRed(), getLineColor().getGreen(), getLineColor().getBlue(), 40);
    }
}
