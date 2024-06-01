package org.rothe.john.working_hours.ui.canvas.util;

import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;

import java.awt.Graphics2D;

public class CanvasInfoImpl implements CanvasInfo {
    private final SpaceTime spaceTime;
    private final RowList rows;
    private int headerWidth = 0;
    private int footerWidth = 0;
    private double hourColumnWidth = 0;

    public CanvasInfoImpl(RowList rows) {
        this.rows = rows;
        this.spaceTime = new SpaceTime(this);
    }

    public void update(Graphics2D g2d) {
        headerWidth = rows.getColumnHeaderWidth(g2d);
        footerWidth = rows.getColumnFooterWidth(g2d);
        hourColumnWidth = calculateHourColumnWidth();
    }

    @Override
    public SpaceTime spaceTIme() {
        return spaceTime;
    }

    @Override
    public int getRowHeaderWidth() {
        return headerWidth;
    }

    @Override
    public int getRowFooterWidth() {
        return footerWidth;
    }

    @Override
    public double getHourColumnWidth() {
        return hourColumnWidth;
    }

    private double calculateHourColumnWidth() {
        if (rows.isEmpty()) {
            return 0.0;
        }
        return (rows.getFirst().getWidth() - (headerWidth + footerWidth)) / 25.0;
    }

}
