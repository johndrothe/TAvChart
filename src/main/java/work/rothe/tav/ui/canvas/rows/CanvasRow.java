package work.rothe.tav.ui.canvas.rows;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;

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

    @Override
    public Dimension getMinimumSize() {
        return max(super.getMinimumSize(), labelHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return max(super.getPreferredSize(), getMinimumSize());
    }

    protected Dimension labelHeight() {
        return new JLabel("Mgpq").getMinimumSize();
    }

    protected static Dimension doubleHeight(Dimension d1) {
        return new Dimension(d1.width, d1.height * 2);
    }

    protected static Dimension max(Dimension d1, Dimension d2) {
        return new Dimension(
                Math.max(d1.width, d2.width),
                Math.max(d1.height, d2.height));
    }

    protected void drawCentered(Graphics2D g2d, String text, double x, double width) {
        drawCentered(g2d, text, x, 0.0, width, getHeight());
    }

    protected static void drawCentered(Graphics2D g2d, String text,
                                       double xOffset, double yOffset,
                                       double width, double height) {
        val bounds = getStrBounds(g2d, text);
        g2d.drawString(
                text,
                (float) (xOffset + width / 2.0 - bounds.getCenterX()),
                (float) (yOffset + height / 2.0 - centeredBaseline(bounds))
        );
    }

    // the y-coordinate used by drawString is the string baseline
    private static double centeredBaseline(Rectangle2D bounds) {
        return bounds.getHeight() / 2.0 + bounds.getY();
    }

    protected void drawRightJustified(Graphics2D g2d, String text, double xOffset, double width) {
        val bounds = getStrBounds(g2d, text);
        g2d.drawString(
                text,
                (float) (xOffset + width - bounds.getWidth()),
                (float) (getHeight() / 2.0 - centeredBaseline(bounds))
        );
    }

    protected void drawLeftJustified(Graphics2D g2d, String text, double x) {
        val bounds = getStrBounds(g2d, text);
        g2d.drawString(text, (float) x, (float) (getHeight() / 2.0 - centeredBaseline(bounds)));
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
