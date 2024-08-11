package org.rothe.john.swc.ui.canvas.rows;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.rothe.john.swc.ui.canvas.util.CanvasCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

@Getter(AccessLevel.PROTECTED)
public abstract class CanvasRow extends JPanel {
    private final CanvasCalculator calculator;
    private final Color textColor = Color.BLACK;
    private final Color lineColor;
    private final Color fillColor;

    protected CanvasRow(CanvasCalculator calculator, Color fillColor, Color lineColor) {
        super(new BorderLayout());
        this.calculator = calculator;
        this.fillColor = fillColor;
        this.lineColor = lineColor;
        setFont(getFont().deriveFont(Font.PLAIN));
        setOpaque(false);
    }

    protected int getRowMinX() {
        return 0;
    }

    protected int getRowMaxX() {
        return getWidth();
    }

    protected int getRowWidth() {
        return getRowMaxX() - getRowMinX();
    }

    protected int getRowHeaderWidth() {
        return calculator.rowHeaderWidth();
    }

    protected int getRowFooterWidth() {
        return calculator.rowFooterWidth();
    }

    protected CanvasCalculator getCalculator() {
        return calculator;
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

        g2d.drawString(text, (float) x, dy);
    }

    private static Rectangle2D getStrBounds(Graphics2D g2d, String text) {
        return g2d.getFontMetrics().getStringBounds(text, g2d);
    }

    protected static void fillRowArea(Graphics g, int x, int width, int height) {
        val arc = height / 3;
        g.fillRoundRect(x + 1, 1, width - 2, height - 2, arc, arc);
    }

    protected static void drawRowBorder(Graphics g, int x, int width, int height) {
        val arc = height / 3;
        g.drawRoundRect(x + 1, 1, width - 2, height - 2, arc, arc);
    }
}
