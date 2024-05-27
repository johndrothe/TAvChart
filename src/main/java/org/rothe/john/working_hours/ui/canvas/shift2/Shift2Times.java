package org.rothe.john.working_hours.ui.canvas.shift2;

import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.Zone;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.rothe.john.working_hours.model.Time.MINUTES_IN_A_DAY;

class Shift2Times {
    private Shift2Times() {

    }

    public static List<Time> get() {
        return stream().toList();
    }

    public static Stream<Time> stream() {
        return dayInFifteenMinuteIncrements().mapToObj(Shift2Times::toTime);
    }

    private static IntStream dayInFifteenMinuteIncrements() {
        return IntStream.iterate(0, value -> value <= MINUTES_IN_A_DAY, value -> value + 15);
    }

    private static Time toTime(int totalMinutesUtc) {
        return Time.at(Zone.utc(), totalMinutesUtc / 60, totalMinutesUtc % 60);
    }
}
