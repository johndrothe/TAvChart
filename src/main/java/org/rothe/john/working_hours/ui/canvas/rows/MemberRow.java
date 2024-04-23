package org.rothe.john.working_hours.ui.canvas.rows;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.util.Palette;
import lombok.Getter;
import lombok.val;

import java.awt.Graphics;
import java.awt.Graphics2D;

@Getter
public class MemberRow extends AbstractZoneRow {
    private final Member member;

    public MemberRow(CanvasInfo canvasInfo, Member member, Palette palette) {
        super(canvasInfo, member.zone(), palette);
        setOpaque(false);
        this.member = member;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        val g2d = (Graphics2D) g;

        g2d.setColor(getTextColor());
        drawCentered(g2d, getDisplayString(), getRowLeftLocation(), getRowDrawWidth());
    }

    @Override
    protected int getRowLeftLocation() {
        return timeToColumnCenter(member.availability().start());
    }

    @Override
    protected int getRowRightLocation() {
        return timeToColumnCenter(member.availability().end());
    }

    private String getDisplayString() {
        return String.format("%s (%s / %s)", member.name(), member.position(), member.location());
    }
}
