package org.rothe.john.working_hours.ui.canvas.st;

import lombok.val;
import org.rothe.john.working_hours.model.Time;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.rothe.john.working_hours.model.Time.MINUTES_IN_A_DAY;
import static org.rothe.john.working_hours.model.Time.fromHoursUtc;

public record TimePair(Time left, Time right) {

    public List<TimePair> splitAround(int hourUtc) {
        if (isSplitBetween(hourUtc)) {
            return List.of(
                    new TimePair(left, fromHoursUtc(left.zone(), hourUtc)),
                    new TimePair(fromHoursUtc(left.zone(), hourUtc), right)
            );
        }

        // not between the two
        return List.of(this);
    }

    public Duration duration() {
        return Duration.ofMinutes(adjustedRightMinutesUtc() - left.totalMinutesInUtc());
    }

    boolean isSplitBetween(int hourUtc) {
        val splitUtc = toSplitMinutes(hourUtc);
        val leftUtc = left.totalMinutesInUtc();
        val rightUtc = adjustedRightMinutesUtc();

        return splitUtc < rightUtc && splitUtc > leftUtc;
    }

    private int toSplitMinutes(int hourUtc) {
        if (hourUtc == 0) {
            return MINUTES_IN_A_DAY;
        }
        return hourUtc * 60;
    }

    int adjustedRightMinutesUtc() {
        return adjustRightMinutesUtc(right.totalMinutesInUtc());
    }

    private int adjustRightMinutesUtc(int rightMinutesUtc) {
        if (rightMinutesUtc < left.totalMinutesInUtc()) {
            return rightMinutesUtc + MINUTES_IN_A_DAY;
        }
        return rightMinutesUtc;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimePair p) {
            return Objects.equals(left, p.left) && Objects.equals(right, p.right);
        }
        return false;
    }

    public boolean contains(Time time) {
        return contains(adjustRightMinutesUtc(time.totalMinutesInUtc()));
    }

    private boolean contains(int totalMinutesUtc) {
        return left.totalMinutesInUtc() <= totalMinutesUtc
                && totalMinutesUtc < adjustedRightMinutesUtc();
    }
}
