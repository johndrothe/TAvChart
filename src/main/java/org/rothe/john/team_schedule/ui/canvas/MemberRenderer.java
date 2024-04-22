package org.rothe.john.team_schedule.ui.canvas;

import org.rothe.john.team_schedule.model.Member;
import org.rothe.john.team_schedule.util.Palette;
import lombok.Getter;
import lombok.val;

import java.awt.Graphics;
import java.awt.Graphics2D;

@Getter
public class MemberRenderer extends ZonedRenderer {
    private final Member member;

    public MemberRenderer(CanvasInfo canvasInfo, Member member, Palette palette) {
        super(canvasInfo, member.zoneId(), palette);
        setOpaque(false);
        this.member = member;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;

        g2d.setColor(getTextColor());
        drawCentered(g2d, getDisplayString(), getRendererLeftLocation(), getRendererDrawWidth());
    }

    @Override
    protected int getRendererLeftLocation() {
        return timeToColumnCenter(member.availability().start());
    }

    @Override
    protected int getRendererRightLocation() {
        return timeToColumnCenter(member.availability().end());
    }

    private String getDisplayString() {
        return String.format("%s (%s / %s)", member.name(), member.position(), member.location());
    }
}
