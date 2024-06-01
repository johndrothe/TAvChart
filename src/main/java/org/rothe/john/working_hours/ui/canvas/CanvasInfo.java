package org.rothe.john.working_hours.ui.canvas;

import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;

public interface CanvasInfo {
    SpaceTime spaceTime();

    int getRowHeaderWidth();

    int getRowFooterWidth();

    double getHourColumnWidth();

    default int getBorderHour() {
        return 0;
    }

    default int getCenterHour() {
        return 12;
    }
}
