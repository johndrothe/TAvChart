package work.rothe.tav.ui.collaboration;

import work.rothe.tav.model.Member;
import work.rothe.tav.model.TimePair;

import java.time.Duration;
import java.util.Comparator;
import java.util.Set;

import static java.util.Comparator.comparing;

public record CollabZone(TimePair time, Set<Member> members) {
    public static Comparator<CollabZone> comparator() {
        return comparing(CollabZone::size).thenComparing(CollabZone::duration);
    }

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
