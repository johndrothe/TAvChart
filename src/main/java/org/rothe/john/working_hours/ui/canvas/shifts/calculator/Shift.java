package org.rothe.john.working_hours.ui.canvas.shifts.calculator;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public record Shift(TimePair time, Set<Member> members) {

    public Duration duration() {
        return time.duration();
    }

    public int size() {
        return members.size();
    }

    public static String toListString(Collection<Shift> shifts) {
        return shifts.stream()
                .map(Shift::toString)
                .collect(joining("\n",
                        "<shifts:" + shifts.size() + ">\n",
                        "</shifts>"));
    }

    @Override
    public String toString() {
        return String.format("(%s - %s)[%s]: {%s}",
                time.left(), time.right(), duration(), Member.toNameList(members())
        );
    }
}
