package org.rothe.john.working_hours.ui.canvas.util;

import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.TimePair;

import java.awt.*;
import java.util.List;

public class CanvasCalculator {
    private final RowList rows;
    private int headerWidth = 0;
    private int footerWidth = 0;
    private double hourColumnWidth = 0;

    public CanvasCalculator(RowList rows) {
        this.rows = rows;
    }

    public void update(Graphics2D g2d) {
        headerWidth = rows.getColumnHeaderWidth(g2d);
        footerWidth = rows.getColumnFooterWidth(g2d);
        hourColumnWidth = calculateHourColumnWidth();
    }

    public List<Boundaries> toCenterBoundaries(TimePair time) {
        return splitAroundBorder(time).stream().map(this::toBoundaries).toList();
    }

    public List<TimePair> splitAroundBorder(TimePair time) {
        return time.splitAround(getBorderHour());
    }

    Boundaries toBoundaries(TimePair pair) {
        return new Boundaries(toColumnCenter(pair.left()),
                toRightColumnCenter(pair.right()));
    }

    public int toColumnStart(Time time) {
        return toColumnStart(time.totalMinutesInUtc());
    }

    public int toColumnStart(int minutesUtc) {
        return rowHeaderWidth() + round(minutesUtc / 60.0 * hourColumnWidth());
    }

    private boolean isRightBoundary(Time time) {
        return time.minuteUtc() == 0 && time.hourUtc() == getBorderHour();
    }

    int toRightColumnCenter(Time time) {
        if (isRightBoundary(time)) {
            return getRightColumnCenter();
        }
        return toColumnCenter(time);
    }

    int getRightColumnCenter() {
        return toColumnCenter(24 * 60, rowHeaderWidth(), hourColumnWidth());
    }

    public int toColumnCenter(Time time) {
        return toColumnCenter(time.totalMinutesInUtc(), rowHeaderWidth(), hourColumnWidth());
    }

    public int toColumnCenter(int minutesUtc) {
        return toColumnCenter(minutesUtc, rowHeaderWidth(), hourColumnWidth());
    }

    static int toColumnCenter(int minutesUtc, int headerWidth, double hourColumnWidth) {
        return headerWidth + round((minutesUtc / 60.0 * hourColumnWidth) + (hourColumnWidth / 2.0));
    }

    private static int round(double value) {
        return (int) Math.round(value);
    }

    public int rowHeaderWidth() {
        return headerWidth;
    }

    public double hourColumnWidth() {
        return hourColumnWidth;
    }

    public int rowFooterWidth() {
        return footerWidth;
    }

    public int getBorderHour() {
        return 0;
    }

    public int getCenterHour() {
        return 12;
    }

    private double calculateHourColumnWidth() {
        if (rows.isEmpty()) {
            return 0.0;
        }
        return (rows.getFirst().getWidth() - (headerWidth + footerWidth)) / 25.0;
    }
}
