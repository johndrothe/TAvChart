package org.rothe.john.swc.ui.canvas;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.rothe.john.swc.event.NewTeamEvent;
import org.rothe.john.swc.event.TeamChangedEvent;
import org.rothe.john.swc.event.TeamListener;
import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.model.Time;
import org.rothe.john.swc.model.Zone;
import org.rothe.john.swc.ui.canvas.painters.CollabZonePainter;
import org.rothe.john.swc.ui.canvas.painters.GridPainter;
import org.rothe.john.swc.ui.canvas.rows.*;
import org.rothe.john.swc.ui.canvas.util.CanvasCalculator;
import org.rothe.john.swc.ui.canvas.util.Palette;
import org.rothe.john.swc.ui.canvas.util.RowList;
import org.rothe.john.swc.util.GBCBuilder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static javax.swing.BorderFactory.*;

public class Canvas extends JPanel implements TeamListener {
    private static final int INSET = 5;
    private final RowList rows = new RowList();
    private final CanvasCalculator calculator;
    private final GridPainter gridPainter;
    private final CollabZonePainter collabZonePainter;
    private Palette palette = null;

    @Getter(AccessLevel.PUBLIC)
    private int borderHour = 0;

    @Getter
    private Team team = null;

    public Canvas() {
        super();

        this.calculator = new CanvasCalculator(this, rows);
        this.gridPainter = new GridPainter(this, calculator);
        this.collabZonePainter = new CollabZonePainter(this, calculator);

        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(border());
        setLayout(new GridBagLayout());
        initialize();
    }

    public void register() {
        Teams.addTeamListener(this);
    }

    public void unregister() {
        Teams.removeTeamListener(this);
    }

    public void setBorderHour(int hour) {
        this.borderHour = Time.normalizeHour(hour);
        revalidate();
        repaint();
    }

    public void teamChanged(TeamChangedEvent event) {
        if(event instanceof NewTeamEvent) {
            borderHour = 0;
        }
        this.team = event.team();
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
        if (isNull(team)) {
            initBlankCanvas();
        } else {
            initTeamCanvas();
        }
        collabZonePainter.initialize();
    }

    private void initBlankCanvas() {
        this.palette = new Palette(List.of());
        removeAll();
    }

    private void initTeamCanvas() {
        val zones = team.getZones();
        this.palette = new Palette(zones);
        removeAll();
        addZones(zones);
        addMembers(team);
        addTransitionsRow(zones);
        addSpacerGlue();
    }

    private void addZones(List<Zone> zones) {
        final Function<Zone, ZoneRow> toRow = zoneId -> new ZoneRow(calculator, zoneId, palette);

        zones.stream()
                .map(toRow)
                .sorted(zoneRowComparator())
                .forEach(this::addRow);
    }

    private void addMembers(Team team) {
        final Function<Member, MemberRow> toRow = member -> new MemberRow(team, member, calculator, palette);

        team.getMembers().stream()
                .map(toRow)
                // optional zone sorting
                // .sorted(zoneRowComparator())
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

    private static GridBagConstraints transitionsConstraints() {
        return defaultConstraints().anchorWest().fillNone()
                .insets(30, INSET, 2, INSET).ipadx(30).ipady(20).build();
    }

    private static GridBagConstraints rowConstraints() {
        return defaultConstraints().insets(0, INSET, 2, INSET).ipady(30).build();
    }

    private static GBCBuilder defaultConstraints() {
        return new GBCBuilder().gridx(0).gridwidth(27).weightx(1.0).fillBoth();
    }

    private Border border() {
        return createCompoundBorder(outerBorder(), createEmptyBorder(5, 0, 0, 0));
    }

    private Border outerBorder() {
        return createCompoundBorder(
                createLineBorder(Color.BLACK, 1),
                createEmptyBorder(10, 10, 10, 10));
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
