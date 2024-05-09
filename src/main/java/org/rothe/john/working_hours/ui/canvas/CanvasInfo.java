package org.rothe.john.working_hours.ui.canvas;

public interface CanvasInfo {
    int getRowHeaderWidth();

    int getRowFooterWidth();

    double getHourColumnWidth();

    int timeToColumnStart(int minutesUtc);

    int timeToColumnCenter(int minutesUtc);

    default int getBorderHour() {
        return 0;
    }

    default int getCenterHour() {
        return 12;
    }
}
