package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;
import org.rothe.john.team_schedule.model.Member;
import org.rothe.john.team_schedule.model.Team;
import org.rothe.john.team_schedule.util.Borders;
import org.rothe.john.team_schedule.util.Palette;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.WEST;
import static java.util.Objects.isNull;

public class Canvas extends JPanel {
    private static final int INSET = 5;
    private static final Comparator<ZonedRenderer> OFFSET_COMPARATOR = Comparator.comparing(ZonedRenderer::getUtcOffset);
    private final RendererList renderers = new RendererList();
    private final CanvasInfoProxy canvasInfo = new CanvasInfoProxy(renderers);
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
        canvasInfo.update((Graphics2D) g);
        if (!renderers.isEmpty()) {
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
        final Function<ZoneId, TimeZoneRenderer> toRenderer = zoneId -> new TimeZoneRenderer(canvasInfo, zoneId, palette);

        zones.stream()
                .map(toRenderer)
                .sorted(OFFSET_COMPARATOR)
                .forEach(this::addRenderer);
    }

    private void addMembers(List<Member> members) {
        final Function<Member, MemberRenderer> toRenderer = member -> new MemberRenderer(canvasInfo, member, palette);

        members.stream()
                .map(toRenderer)
                .sorted(OFFSET_COMPARATOR)
                .forEach(this::addRenderer);
    }

    private void addRenderer(AbstractRenderer renderer) {
        add(renderer, rendererConstraints());
        renderers.add(renderer);
    }

    private void addTransitionsRenderer(List<ZoneId> zoneIds) {
        val renderer = new TransitionsRenderer(canvasInfo, zoneIds);
        add(renderer, transitionsConstraints());
        renderers.add(renderer);
    }

    private void addSpacerGlue() {
        add(Box.createGlue(), new GridBagConstraints(0, -1, 27, 1,
                1.0, 1.0, CENTER, BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
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

    private static class CanvasInfoProxy implements CanvasInfo {
        private final RendererList renderers;
        private int headerWidth = 0;
        private int footerWidth = 0;

        public CanvasInfoProxy(RendererList renderers) {
            this.renderers = renderers;
        }

        @Override
        public int getRowHeaderWidth() {
            return headerWidth;
        }

        @Override
        public int getRowFooterWidth() {
            return footerWidth;
        }

        public void update(Graphics2D g2d) {
            headerWidth = renderers.getColumnHeaderWidth(g2d);
            footerWidth = renderers.getColumnFooterWidth(g2d);
        }
    }
}
