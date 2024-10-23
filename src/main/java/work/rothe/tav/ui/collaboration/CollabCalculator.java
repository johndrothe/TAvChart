package work.rothe.tav.ui.collaboration;

import work.rothe.tav.model.Member;
import work.rothe.tav.model.Time;
import work.rothe.tav.model.TimePair;
import work.rothe.tav.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class CollabCalculator {
    private final List<Member> members;

    private CollabCalculator(Collection<Member> members) {
        this.members = members.stream().toList();
    }

    public static CollabCalculator of(Collection<Member> members) {
        return new CollabCalculator(members);
    }

    public List<CollabZone> largest() {
        return findZones().max(CollabZone.comparator()).map(List::of).orElse(List.of());
    }

    public List<CollabZone> zones() {
        return findZones().toList();
    }

    private Stream<CollabZone> findZones() {
        return Pair.stream(prependLast(shiftChanges()))
                .filter(p -> p.left().hasMembers())
                .map(this::toCollabZone);
    }

    private CollabZone toCollabZone(Pair<ShiftChange> pair) {
        return new CollabZone(toTimePair(pair), Set.copyOf(pair.left().members()));
    }

    private TimePair toTimePair(Pair<ShiftChange> pair) {
        return new TimePair(pair.left().time(), pair.right().time());
    }

    public List<ShiftChange> shiftChanges() {
        return shiftChangeTimes().stream()
                .map(this::toShiftChange)
                .toList();
    }

    private ShiftChange toShiftChange(Time time) {
        return new ShiftChange(presentAt(time), time);
    }

    private List<Member> presentAt(final Time time) {
        return members.stream().filter(m -> isPresentAt(m, time)).toList();
    }

    private boolean isPresentAt(Member member, Time time) {
        return member.normal().contains(time);
    }

    private List<Time> shiftChangeTimes() {
        return normalTimes()
                .sorted(comparing(Time::totalMinutesInUtc))
                .distinct()
                .toList();
    }

    private Stream<Time> normalTimes() {
        return members.stream()
                .map(Member::normal)
                .flatMap(n -> Stream.of(n.left(), n.right()))
                .map(Time::inUtc);
    }

    private static <T> List<T> prependLast(List<T> list) {
        if (list.isEmpty()) {
            return List.of();
        }
        return Stream.concat(Stream.of(list.getLast()), list.stream()).toList();
    }
}
