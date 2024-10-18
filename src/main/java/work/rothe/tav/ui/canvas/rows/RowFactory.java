package work.rothe.tav.ui.canvas.rows;

import work.rothe.tav.model.Document;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.canvas.util.Palette;
import work.rothe.tav.util.GBCBuilder;

import java.awt.GridBagConstraints;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

public class RowFactory {
    private static final int INSET = 5;
    private static final double ROW_PADDING = 4.0;

    private final Canvas canvas;
    private final CanvasCalculator calculator;
    private final Document document;
    private final List<Zone> zones;
    private final Palette palette;

    private RowFactory(Canvas canvas, CanvasCalculator calculator) {
        this.canvas = canvas;
        this.calculator = calculator;
        this.document = canvas.getDocument();
        this.zones = document.zones();
        this.palette = new Palette(zones);
    }

    public static RowFactory of(Canvas canvas, CanvasCalculator calculator) {
        return new RowFactory(canvas, calculator);
    }

    public void addRows() {
        addTeamNameRow();
        addZones();
        addMembers();
        addTransitionsRow();
    }

    private void addTeamNameRow() {
        canvas.add(new TitleRow(calculator, document), titleConstraints());
    }

    private void addZones() {
        zones.stream()
                .map(id -> new ZoneRow(document, calculator, id, palette))
                .sorted(zoneRowComparator())
                .forEach(this::add);
    }

    private void addMembers() {
        document.members().stream()
                .map(m -> new MemberRow(document, m, calculator, palette))
                .forEach(this::add);
    }

    private void add(CanvasRow row) {
        canvas.add(row, rowConstraints());
    }

    private void addTransitionsRow() {
        canvas.add(new ZoneTransitionsRow(calculator, zones), transitionsConstraints());
    }

    private int uiScaled(double pixels) {
        return canvas.uiScaled(pixels);
    }

    private GridBagConstraints transitionsConstraints() {
        return defaultConstraints().anchorWest().fillNone()
                .insets(canvas.getRowHeightMinimum(), inset(), uiScaled(2), inset())
                .ipadx(inset(10))
                .ipady(inset(2))
                .build();
    }

    private GridBagConstraints titleConstraints() {
        return defaultConstraints()
                .insets(0, inset(), inset(), inset())
                .ipady(getRowPadding())
                .build();
    }

    private GridBagConstraints rowConstraints() {
        return defaultConstraints()
                .insets(0, inset(), uiScaled(2), inset())
                .ipady(getRowPadding())
                .build();
    }

    private static GBCBuilder defaultConstraints() {
        return new GBCBuilder().gridx(0).gridwidth(27).weightx(1.0).fillBoth();
    }

    private int getRowPadding() {
        return uiScaled(ROW_PADDING);
    }

    private Comparator<AbstractZoneRow> zoneRowComparator() {
        return comparing(AbstractZoneRow::getOffsetHours);
    }

    private int inset(double multiplier) {
        return uiScaled(INSET * multiplier);
    }

    private int inset() {
        return uiScaled(INSET);
    }
}
