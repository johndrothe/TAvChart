package org.rothe.john.working_hours.ui.collaboration;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

import java.time.Duration;
import java.util.Set;

public record CollabZone(TimePair time, Set<Member> members) {

    public Duration duration() {
        return time.duration();
    }

    public int size() {
        return members.size();
    }

    @Override
    public String toString() {
        return String.format("(%s - %s)[%s]: {%s}",
                time.left(), time.right(), duration(), Member.toNameList(members())
        );
    }
}
