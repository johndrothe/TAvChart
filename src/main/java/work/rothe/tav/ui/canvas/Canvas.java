package work.rothe.tav.ui.canvas;

import lombok.Getter;
import lombok.val;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.model.Time;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.painters.CollabZonePainter;
import work.rothe.tav.ui.canvas.painters.GridPainter;
import work.rothe.tav.ui.canvas.rows.AbstractZoneRow;
import work.rothe.tav.ui.canvas.rows.CanvasRow;
import work.rothe.tav.ui.canvas.rows.MemberRow;
import work.rothe.tav.ui.canvas.rows.ZoneRow;
import work.rothe.tav.ui.canvas.rows.ZoneTransitionsRow;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.canvas.util.Palette;
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
import java.awt.RenderingHints;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class Canvas extends JPanel implements DocumentListener {
    private static final int INSET = 5;
    private static final double BASE_ROW_HEIGHT = 30.0;
    private final Settings settings;
    private final RowList rows = new RowList();
    private final CanvasCalculator calculator;
    private final GridPainter gridPainter;
    private final CollabZonePainter collabZonePainter;
    private Palette palette = null;

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
        return (int) Math.ceil(BASE_ROW_HEIGHT * settings.getUiScale() / 100.0);
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
        revalidate();
        repaint();
    }

    @Override
    protected void paintChildren(Graphics g) {
        calculator.update((Graphics2D) g);

        applyRenderingHints((Graphics2D) g);

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

    private void initialize() {
        rows.clear();
        if (isNull(document)) {
            initBlankCanvas();
        } else {
            initCanvas();
        }
        collabZonePainter.initialize();
    }

    private void initBlankCanvas() {
        this.palette = new Palette(List.of());
        removeAll();
    }

    private void initCanvas() {
        val zones = document.zones();
        this.palette = new Palette(zones);
        removeAll();
        addZones(zones);
        addMembers(document);
        addTransitionsRow(zones);
        addSpacerGlue();
    }

    private void addZones(List<Zone> zones) {
        final Function<Zone, ZoneRow> toRow = zoneId -> new ZoneRow(document, calculator, zoneId, palette);

        zones.stream()
                .map(toRow)
                .sorted(zoneRowComparator())
                .forEach(this::addRow);
    }

    private void addMembers(Document document) {
        final Function<Member, MemberRow> toRow = member -> new MemberRow(document, member, calculator, palette);

        document.members().stream()
                .map(toRow)
                .forEach(this::addRow);
    }

    private Comparator<AbstractZoneRow> zoneRowComparator() {
        return comparing(AbstractZoneRow::getOffsetHours);
    }

    private void addRow(CanvasRow row) {
        add(row, rowConstraints());
        rows.add(row);
    }

    private void addTransitionsRow(List<Zone> zoneIds) {
        val row = new ZoneTransitionsRow(calculator, zoneIds);
        add(row, transitionsConstraints());
        rows.add(row);
    }

    private void addSpacerGlue() {
        add(Box.createGlue(), spacerConstraints());
    }

    private static GridBagConstraints spacerConstraints() {
        return defaultConstraints().weighty(1.0).insets(0).build();
    }

    private GridBagConstraints transitionsConstraints() {
        return defaultConstraints().anchorWest().fillNone()
                .insets(getRowHeightMinimum(), INSET, 2, INSET)
                .ipadx(30).ipady(20).build();
    }

    private GridBagConstraints rowConstraints() {
        return defaultConstraints().insets(0, INSET, 2, INSET).ipady(getRowHeightMinimum()).build();
    }

    private static GBCBuilder defaultConstraints() {
        return new GBCBuilder().gridx(0).gridwidth(27).weightx(1.0).fillBoth();
    }

    private Border border() {
        return createCompoundBorder(outerBorder(), createEmptyBorder(5, 0, 0, 0));
    }

    private Border outerBorder() {
        return createEmptyBorder(10, 10, 10, 10);
    }

    private static void applyRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }
}
