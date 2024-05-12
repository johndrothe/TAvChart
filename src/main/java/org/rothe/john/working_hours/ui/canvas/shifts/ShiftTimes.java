package org.rothe.john.working_hours.ui.canvas.shifts;

import org.rothe.john.working_hours.model.Time;

import java.util.stream.IntStream;

class ShiftTimes {
    private ShiftTimes() {

    }

    public static int[] get() {
        return stream().toArray();
    }

    public static IntStream stream() {
        return IntStream.iterate(0,
                value -> value <= Time.MINUTES_IN_A_DAY,
                value -> value + 15);
    }
}
