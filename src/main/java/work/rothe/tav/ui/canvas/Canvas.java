package work.rothe.tav.ui.canvas;

import lombok.Getter;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Time;
import work.rothe.tav.ui.canvas.painters.CollabZonePainter;
import work.rothe.tav.ui.canvas.painters.GridPainter;
import work.rothe.tav.ui.canvas.rows.CanvasRow;
import work.rothe.tav.ui.canvas.rows.RowBuilder;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.canvas.util.Rendering;
import work.rothe.tav.ui.canvas.util.RowList;
import work.rothe.tav.util.GBCBuilder;
import work.rothe.tav.util.Settings;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import static java.util.Objects.nonNull;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class Canvas extends JPanel implements DocumentListener {
    private static final double BASE_ROW_HEIGHT = 30.0;
    private final Settings settings;
    private final RowList rows = new RowList();
    private final CanvasCalculator calculator;
    private final GridPainter gridPainter;
    private final CollabZonePainter collabZonePainter;

    private int borderHourOffset = 0;

    @Getter
    private Document document = null;

    public Canvas(Settings settings) {
        super();
        this.settings = settings;
        this.calculator = new CanvasCalculator(this, rows);
        this.gridPainter = new GridPainter(this, calculator);
        this.collabZonePainter = new CollabZonePainter(this, calculator);

        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(border());
        setLayout(new GridBagLayout());
        initialize();
    }

    public int getRowHeightMinimum() {
        return uiScaled(BASE_ROW_HEIGHT);
    }

    public void add(CanvasRow row, GridBagConstraints constraints) {
        super.add(rows.add(row), constraints);
    }

    public int uiScaled(double pixels) {
        return (int) Math.ceil(pixels * settings.getUiScale() / 100.0);
    }

    public void register() {
        Documents.addDocumentListener(this);
    }

    public void unregister() {
        Documents.removeDocumentListener(this);
    }

    public void setBorderHourOffset(int offset) {
        this.borderHourOffset = offset;
        revalidate();
        repaint();
    }

    public int getBorderHour() {
        return Time.normalizeHour(document.borderHour() + borderHourOffset);
    }

    public void documentChanged(DocumentChangedEvent event) {
        this.document = event.document();
        this.borderHourOffset = 0;
        initialize();
        setPreferredSize(document.canvasSize());
        revalidate();
        repaint();
    }

    private void initialize() {
        rows.clear();
        removeAll();
        if (nonNull(document)) {
            RowBuilder.of(this)
                    .calculator(calculator)
                    .build()
                    .forEach(e -> add(e.row(), e.constraints()));
            add(Box.createGlue(), spacerConstraints());
        }
        collabZonePainter.initialize();
    }

    @Override
    protected void paintChildren(Graphics g) {
        calculator.update((Graphics2D) g);

        Rendering.applyHints((Graphics2D) g);

        paintUnder((Graphics2D) g);
        super.paintChildren(g);
        paintOver((Graphics2D) g);
    }

    private void paintOver(Graphics2D g2d) {
        if (!rows.isEmpty()) {
            collabZonePainter.paintOver(g2d, rows.getFirst());
        }
    }

    private void paintUnder(Graphics2D g2d) {
        if (!rows.isEmpty()) {
            gridPainter.paintGrid(g2d, rows.getFirst());
            collabZonePainter.paintUnder(g2d, rows.getFirst());
        }
    }

    private static GridBagConstraints spacerConstraints() {
        return new GBCBuilder()
                .gridx(0).gridwidth(27).weightx(1.0)
                .weighty(1.0)
                .fillBoth().insets(0)
                .build();
    }

    private Border border() {
        return createCompoundBorder(outerBorder(), createEmptyBorder(5, 0, 0, 0));
    }

    private Border outerBorder() {
        return createEmptyBorder(10, 10, 10, 10);
    }
}
