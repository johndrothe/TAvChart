package org.rothe.john.working_hours.ui.canvas.st;

import lombok.AllArgsConstructor;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;

import java.util.List;

@AllArgsConstructor()
public class SpaceTime {
    private final CanvasInfo canvasInfo;

    public List<Boundaries> toCenterBoundaries(TimePair time) {
        return splitAroundBorder(time).stream().map(this::toBoundaries).toList();
    }

    public List<TimePair> splitAroundBorder(TimePair time) {
        return time.splitAround(canvasInfo.getBorderHour());
    }

    Boundaries toBoundaries(TimePair pair) {
        return new Boundaries(toColumnCenter(pair.left()),
                toRightColumnCenter(pair.right()));
    }

    public int toColumnStart(Time time) {
        return toColumnStart(time.totalMinutesInUtc());
    }

    public int toColumnStart(int minutesUtc) {
        return headerWidth() + round(minutesUtc / 60.0 * hourColumnWidth());
    }

    private boolean isRightBoundary(Time time) {
        return time.minuteUtc() == 0 && time.hourUtc() == canvasInfo.getBorderHour();
    }

    int toRightColumnCenter(Time time) {
        if(isRightBoundary(time)) {
            return getRightColumnCenter();
        }
        return toColumnCenter(time);
    }

    int getRightColumnCenter() {
        return toColumnCenter(24 * 60, headerWidth(), hourColumnWidth());
    }

    public int toColumnCenter(Time time) {
        return toColumnCenter(time.totalMinutesInUtc(), headerWidth(), hourColumnWidth());
    }

    public int toColumnCenter(int minutesUtc) {
        return toColumnCenter(minutesUtc, headerWidth(), hourColumnWidth());
    }

    static int toColumnCenter(int minutesUtc, int headerWidth, double hourColumnWidth) {
        return headerWidth + round((minutesUtc / 60.0 * hourColumnWidth) + (hourColumnWidth / 2.0));
    }

    private static int round(double value) {
        return (int)Math.round(value);
    }

    private int headerWidth() {
        return canvasInfo.getRowHeaderWidth();
    }

    private double hourColumnWidth() {
        return canvasInfo.getHourColumnWidth();
    }
}
