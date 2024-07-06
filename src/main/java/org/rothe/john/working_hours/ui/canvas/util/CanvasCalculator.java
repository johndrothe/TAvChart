package org.rothe.john.working_hours.ui.canvas.util;

import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.TimePair;
import org.rothe.john.working_hours.ui.canvas.Canvas;

import java.awt.Graphics2D;
import java.util.List;

public class CanvasCalculator {
    private final Canvas canvas;
    private final RowList rows;
    private int headerWidth = 0;
    private int footerWidth = 0;
    private double hourColumnWidth = 0;

    public CanvasCalculator(Canvas canvas, RowList rows) {
        this.canvas = canvas;
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
        try {
            return new Boundaries(toColumnCenter(pair.left()), toRightColumnCenter(pair.right()));
        } catch (java.lang.IllegalArgumentException e) {
            System.err.printf("Failed to generate boundaries for [%s, %s] (UTC) at [%d, %d] (pixels) with border hour %d.%n",
                    pair.left().toUtcString(),
                    pair.right().toUtcString(),
                    toColumnCenter(pair.left()),
                    toRightColumnCenter(pair.right()),
                    getBorderHour());
            throw e;
        }
    }

    public int toColumnStart(Time time) {
        return toColumnStart(time.totalMinutesInUtc());
    }

    public int toColumnStart(int minutesUtc) {
        return rowHeaderWidth() + round(toMinuteLocation(minutesUtc) / 60.0 * hourColumnWidth());
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
        return toColumnCenter((getBorderHour() + 24) * 60, rowHeaderWidth(), hourColumnWidth());
    }

    public int toColumnCenter(Time time) {
        return toColumnCenter(time.totalMinutesInUtc(), rowHeaderWidth(), hourColumnWidth());
    }

    public int toColumnCenter(int minutesUtc) {
        return toColumnCenter(minutesUtc, rowHeaderWidth(), hourColumnWidth());
    }

    int toColumnCenter(int minutesUtc, int headerWidth, double hourColumnWidth) {
        return headerWidth + round((toMinuteLocation(minutesUtc) / 60.0 * hourColumnWidth) + (hourColumnWidth / 2.0));
    }

    private int toMinuteLocation(int minutesUtc) {
        int minuteLocation = minutesUtc - getBorderHour() * 60;
        if (minuteLocation < 0) {
            return minuteLocation + Time.MINUTES_IN_A_DAY;
        }
        return minuteLocation;
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
        return canvas.getBorderHour();
    }

    public void setBorderHour(int hour) {
        canvas.setBorderHour(hour);
    }

    private double calculateHourColumnWidth() {
        if (rows.isEmpty()) {
            return 0.0;
        }
        return (rows.getFirst().getWidth() - (headerWidth + footerWidth)) / 25.0;
    }
}
