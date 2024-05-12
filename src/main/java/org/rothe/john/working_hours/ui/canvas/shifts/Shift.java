package org.rothe.john.working_hours.ui.canvas.shifts;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public record Shift(int start, int end, Set<Member> members) {
    public int duration() {
        if (end < start) {
            return start - (end + Time.MINUTES_IN_A_DAY);
        }
        return end - start;
    }

    public int size() {
        return members.size();
    }

    public static String toListString(Collection<Shift> shifts) {
        return shifts.stream()
                .map(Shift::toString)
                .collect(joining("\n",
                        "<largest_shifts:" + shifts.size() + ">\n",
                        "</largest_shifts>"));
    }

    @Override
    public String toString() {
        return String.format("(%s - %s): [%s]",
                Time.toClockFormat(start()),
                Time.toClockFormat(end()),
                Member.toNameList(members())
        );
    }
}
