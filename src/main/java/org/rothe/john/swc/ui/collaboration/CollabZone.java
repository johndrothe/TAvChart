package org.rothe.john.swc.ui.collaboration;

import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.model.TimePair;

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
