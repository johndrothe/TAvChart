package org.rothe.john.working_hours.ui.canvas.shifts;

import java.util.Arrays;
import java.util.stream.IntStream;

class ShiftTimes {
    private static final int[] SHIFT_TIMES = shiftTimes();

    private ShiftTimes() {

    }

    public static int[] get() {
        return SHIFT_TIMES;
    }

    public static IntStream stream() {
        return Arrays.stream(SHIFT_TIMES);
    }

    private static int[] shiftTimes() {
        int[] values = new int[24 * 4 + 1];

        for (int i = 0; i < values.length; ++i) {
            values[i] = i * 15;
        }
        return values;
    }

}
