package org.rothe.john.working_hours.ui.canvas;

import lombok.Getter;
import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.canvas.rows.AbstractZoneRow;
import org.rothe.john.working_hours.ui.canvas.rows.CanvasRow;
import org.rothe.john.working_hours.ui.canvas.rows.MemberRow;
import org.rothe.john.working_hours.ui.canvas.rows.ZoneRow;
import org.rothe.john.working_hours.ui.canvas.rows.ZoneTransitionsRow;
import org.rothe.john.working_hours.ui.canvas.shifts.ShiftPainter;
import org.rothe.john.working_hours.ui.canvas.util.CanvasInfoImpl;
import org.rothe.john.working_hours.ui.canvas.util.GridPainter;
import org.rothe.john.working_hours.ui.canvas.util.Palette;
import org.rothe.john.working_hours.ui.canvas.util.RowList;
import org.rothe.john.working_hours.ui.util.GBCBuilder;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class Canvas extends JPanel {
    private static final int INSET = 5;
    private final RowList rows = new RowList();
    private final CanvasInfoImpl canvasInfo = new CanvasInfoImpl(rows);
    private final GridPainter gridPainter = new GridPainter(this, canvasInfo);
    private final ShiftPainter shiftPainter = new ShiftPainter(this, canvasInfo);
    private Palette palette = null;

    @Getter
    private Team team = null;

    public Canvas() {
        super();
        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(border());
        setLayout(new GridBagLayout());
        initialize();
    }

    public void setTeam(Team team) {
        this.team = team;
        initialize();
        revalidate();
        repaint();
    }

    @Override
    protected void paintChildren(Graphics g) {
        canvasInfo.update((Graphics2D) g);
        paintUnder((Graphics2D) g);
        super.paintChildren(g);
        paintOver((Graphics2D) g);
    }

    private void paintOver(Graphics2D g2d) {
        if (!rows.isEmpty()) {
            shiftPainter.paintOver(g2d, rows.getFirst());
        }
    }

    private void paintUnder(Graphics2D g2d) {
        if (!rows.isEmpty()) {
            gridPainter.paintGrid(g2d, rows.getFirst());
            shiftPainter.paintUnder(g2d, rows.getFirst());
        }
    }

    private void initialize() {
        rows.clear();
        if (isNull(team)) {
            initBlankCanvas();
        } else {
            initTeamCanvas();
        }
        shiftPainter.initialize();
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
        addMembers(team.getMembers());
        addTransitionsRow(zones);
        addSpacerGlue();
    }

    private void addZones(List<Zone> zones) {
        final Function<Zone, ZoneRow> toRow = zoneId -> new ZoneRow(canvasInfo, zoneId, palette);

        zones.stream()
                .map(toRow)
                .sorted(zoneRowComparator())
                .forEach(this::addRow);
    }

    private void addMembers(List<Member> members) {
        final Function<Member, MemberRow> toRow = member -> new MemberRow(canvasInfo, member, palette);

        members.stream()
                .map(toRow)
                .sorted(zoneRowComparator())
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
        val row = new ZoneTransitionsRow(canvasInfo, zoneIds);
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
}
