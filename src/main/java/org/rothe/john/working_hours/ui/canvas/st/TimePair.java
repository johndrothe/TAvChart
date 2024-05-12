package org.rothe.john.working_hours.ui.canvas.st;

import lombok.val;
import org.rothe.john.working_hours.model.Time;

import java.util.List;
import java.util.Objects;

import static org.rothe.john.working_hours.model.Time.fromHoursUtc;

public record TimePair(Time left, Time right) {

    public List<TimePair> splitAround(int hourUtc) {
        if(isSplitBetween(hourUtc)) {
            return List.of(
                    new TimePair(left, fromHoursUtc(left.zone(), hourUtc)),
                    new TimePair(fromHoursUtc(left.zone(), hourUtc), right)
            );
        }

        // not between the two
        return List.of(this);
    }

    boolean isSplitBetween(int hourUtc) {
        val splitUtc = toSplitMinutes(hourUtc);
        val leftUtc = left.totalMinutesInUtc();
        val rightUtc = adjustedRightMinutesUtc();
        
        return splitUtc < rightUtc && splitUtc > leftUtc;
    }

    private int toSplitMinutes(int hourUtc){
        if(hourUtc == 0) {
            return Time.MINUTES_IN_A_DAY;
        }
        return hourUtc * 60;
    }

    int adjustedRightMinutesUtc() {
        if (right.totalMinutesInUtc() < left.totalMinutesInUtc()) {
            return right.totalMinutesInUtc() + Time.MINUTES_IN_A_DAY;
        }
        return right.totalMinutesInUtc();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TimePair p){
            return Objects.equals(left, p.left) && Objects.equals(right, p.right);
        }
        return false;
    }
}
