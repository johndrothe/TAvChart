package org.rothe.john.working_hours.ui.canvas.shifts;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Zone;

import java.util.Set;

public record Shift(int start, int end, Set<Member> members) {


    public int duration() {
        if (end < start) {
            return start - (end + Zone.MINUTES_IN_A_DAY);
        }
        return end - start;
    }

    public int size() {
        return members.size();
    }
}
