package org.rothe.john.working_hours.ui.canvas.util;

import org.rothe.john.working_hours.ui.canvas.CanvasInfo;

import java.awt.Graphics2D;

public class CanvasInfoImpl implements CanvasInfo {
    private final RowList rows;
    private int headerWidth = 0;
    private int footerWidth = 0;
    private double hourColumnWidth = 0;

    public CanvasInfoImpl(RowList rows) {
        this.rows = rows;
    }

    public void update(Graphics2D g2d) {
        headerWidth = rows.getColumnHeaderWidth(g2d);
        footerWidth = rows.getColumnFooterWidth(g2d);
        hourColumnWidth = calculateHourColumnWidth();
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

    @Override
    public int timeToColumnStart(int minutesUtc) {
        return headerWidth + (int) Math.round(minutesUtc / 60.0 * hourColumnWidth);
    }

    @Override
    public int timeToColumnCenter(int minutesUtc) {
        return headerWidth + (int) Math.round(minutesUtc / 60.0 * hourColumnWidth + hourColumnWidth / 2.0);
    }

    private double calculateHourColumnWidth() {
        if (rows.isEmpty()) {
            return 0.0;
        }
        return (rows.getFirst().getWidth() - (headerWidth + footerWidth)) / 25.0;
    }

}
