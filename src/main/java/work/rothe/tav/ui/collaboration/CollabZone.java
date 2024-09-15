package work.rothe.tav.ui.collaboration;

import work.rothe.tav.model.Member;
import work.rothe.tav.model.TimePair;

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
