package org.rothe.john.team_schedule.ui.canvas;

import org.rothe.john.team_schedule.model.Member;
import org.rothe.john.team_schedule.model.Team;
import org.rothe.john.team_schedule.util.Palette;
import lombok.val;
import org.rothe.john.team_schedule.util.Borders;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.util.Objects.isNull;

public class Canvas extends JPanel {
    private static final int INSET = 5;
    private final List<AbstractRenderer> renderers = new ArrayList<>();
    private Palette palette = null;
    private Team team = null;
    private int zoneIdWidth;
    private int zoneNameWidth;

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
        updateRendererColumnSizes((Graphics2D) g);
        super.paintChildren(g);
    }

//    private static void activateTransparency(Graphics2D g) {
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.75f));
//    }

    private void updateRendererColumnSizes(Graphics2D g2d) {
        zoneIdWidth = calculateZoneColumnWidth(ZonedRenderer::getZoneIdString, g2d);
        zoneNameWidth = calculateZoneColumnWidth(ZonedRenderer::getLocationDisplayString, g2d);

        renderers.forEach(r -> r.setZoneIdColumnWidth(zoneIdWidth));
        renderers.forEach(r -> r.setZoneNameColumnWidth(zoneNameWidth));
    }

    private int calculateZoneColumnWidth(Function<ZonedRenderer, String> getter, Graphics2D g2d) {
        return getZonedRenderers()
                .map(getter)
                .map(s -> getRenderedStringWidth(s, g2d))
                .max(Comparator.naturalOrder())
                .orElse(0) + 20;
    }

    private Stream<ZonedRenderer> getZonedRenderers() {
        return renderers.stream()
                .filter(r -> r instanceof ZonedRenderer)
                .map(r -> (ZonedRenderer) r);
    }

    private static int getRenderedStringWidth(String text, Graphics2D g) {
        return (int) Math.ceil(g.getFontMetrics().getStringBounds(text, g).getWidth());
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

    private static GridBagConstraints rendererConstraints() {
        return new GridBagConstraints(0, -1, 27, 1,
                1.0, 0.0, CENTER, BOTH,
                rendererInsets(), 0, 30);
    }

    private static Insets rendererInsets() {
        return new Insets(0, INSET, 2, INSET);
    }

}
