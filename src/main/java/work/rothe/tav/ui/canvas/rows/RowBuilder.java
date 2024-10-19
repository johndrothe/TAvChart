package work.rothe.tav.ui.canvas.rows;

import work.rothe.tav.model.Document;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.ui.canvas.util.Palette;
import work.rothe.tav.util.GBCBuilder;

import java.awt.GridBagConstraints;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class RowBuilder {
    private static final int INSET = 5;
    private static final double ROW_PADDING = 4.0;

    private final Document document;
    private final List<Zone> zones;
    private final Palette palette;
    private CanvasCalculator calculator;

    private RowBuilder(Document document) {
        this.document = document;
        this.zones = document.zones();
        this.palette = new Palette(zones);
    }

    public record Entry(CanvasRow row, GridBagConstraints constraints) {
    }

    public static RowBuilder of(Document document) {
        return new RowBuilder(document);
    }

    public RowBuilder calculator(CanvasCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    public Stream<Entry> build() {
        return concat(titleRow(), zoneRows(), memberRows(), transitionsRow());
    }

    private Stream<Entry> titleRow() {
        return Stream.of(new Entry(new TitleRow(calculator, document), titleConstraints()));
    }

    private Stream<Entry> zoneRows() {
        return zones.stream()
                .map(id -> new ZoneRow(document, calculator, id, palette))
                .sorted(zoneRowComparator())
                .map(this::toRow);
    }

    private Stream<Entry> memberRows() {
        return document.members().stream()
                .map(m -> new MemberRow(document, m, calculator, palette))
                .map(this::toRow);
    }

    private Stream<Entry> transitionsRow() {
        return Stream.of(new Entry(new ZoneTransitionsRow(calculator, zones), transitionsConstraints()));
    }

    private static Stream<Entry> concat(Stream<Entry> first,
                                        Stream<Entry> second,
                                        Stream<Entry> third,
                                        Stream<Entry> fourth) {
        return Stream.concat(Stream.concat(Stream.concat(first, second), third), fourth);
    }

    private Entry toRow(CanvasRow row) {
        return new Entry(row, rowConstraints());
    }

    private int uiScaled(double pixels) {
        return calculator.uiScaled(pixels);
    }

    private GridBagConstraints transitionsConstraints() {
        return defaultConstraints().anchorWest().fillNone()
                .insets(calculator.getBaseRowHeight(), inset(), uiScaled(2), inset())
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
