package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;
import org.rothe.john.team_schedule.model.Member;
import org.rothe.john.team_schedule.model.Team;
import org.rothe.john.team_schedule.util.Borders;
import org.rothe.john.team_schedule.util.Palette;
import org.rothe.john.team_schedule.util.Zones;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Comparator;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;
import static java.util.Objects.isNull;

public class Canvas extends JPanel {
    private static final int INSET = 5;
    private final RendererList renderers = new RendererList();
    private Palette palette = null;
    private Team team = null;

    public Canvas() {
        super();
        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(Borders.raised());
        setLayout(new GridBagLayout());
        initialize();
    }

    @Override
    protected void paintChildren(Graphics g) {
        renderers.updateColumnWidths((Graphics2D) g);
        if(!renderers.isEmpty()) {
            GridPainter.paintGrid((Graphics2D) g, this, renderers.getFirst());
        }
        super.paintChildren(g);
    }


    public void setTeam(Team team) {
        this.team = team;
        initialize();
    }

    private void initialize() {
        renderers.clear();
        if (isNull(team)) {
            initBlankCanvas();
        } else {
            initTeamCanvas();
        }
    }

    private void initBlankCanvas() {
        this.palette = new Palette(List.of());
        removeAll();
    }

    private void initTeamCanvas() {
        val zoneIds = team.getZoneIds();
        this.palette = new Palette(zoneIds);
        removeAll();
        addZones(zoneIds);
        addMembers(team.getMembers());
        addTransitionsRenderer(zoneIds);
        addSpacerGlue();
    }

    private void addZones(List<ZoneId> zones) {
        zones.stream()
                .map(zoneId -> new TimeZoneRenderer(zoneId, palette))
                .sorted(Comparator.comparing(ZonedRenderer::getUtcOffset))
                .forEach(this::addRenderer);
    }

    private void addMembers(List<Member> members) {
        members.stream()
                .map(member -> new MemberRenderer(member, palette))
                .sorted(Comparator.comparing(ZonedRenderer::getUtcOffset))
                .forEach(this::addRenderer);
    }

    private void addRenderer(AbstractRenderer renderer) {
        add(renderer, rendererConstraints());
        renderers.add(renderer);
    }

    private void addTransitionsRenderer(List<ZoneId> zoneIds) {
        val renderer = new TransitionsRenderer(zoneIds);
        add(renderer, transitionsConstraints());
        renderers.add(renderer);
    }

    private void addSpacerGlue() {
        add(Box.createGlue(), new GridBagConstraints(0, -1, 27, 1,
                1.0, 1.0, CENTER, BOTH,
                new Insets(0,0,0,0), 0, 0));
    }

    private static GridBagConstraints transitionsConstraints() {
        return new GridBagConstraints(0, -1, 27, 1,
                0.0, 0.0, WEST, NONE,
                new Insets(30, INSET, 2, INSET), 20, 20);
    }

    private static GridBagConstraints rendererConstraints() {
        return new GridBagConstraints(0, -1, 27, 1,
                1.0, 0.0, CENTER, BOTH,
                rendererInsets(), 0, 30);
    }

    private static Insets rendererInsets() {
        return new Insets(0, INSET, 2, INSET);
    }
}
