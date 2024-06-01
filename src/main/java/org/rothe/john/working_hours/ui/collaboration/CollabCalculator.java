package org.rothe.john.working_hours.ui.collaboration;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;
import org.rothe.john.working_hours.util.Pair;

import java.util.*;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class CollabCalculator {
    private final List<Member> members;

    private CollabCalculator(Collection<Member> members) {
        this.members = members.stream().toList();
    }

    public static CollabCalculator of(Collection<Member> members) {
        return new CollabCalculator(members);
    }

    public List<CollabZone> largest() {
        return findCollabZones().stream()
                .sorted(comparator())
                .limit(1)
                .toList();
    }

    private static Comparator<CollabZone> comparator() {
        return Comparator.comparing(CollabZone::size)
                .reversed()
                .thenComparing(CollabZone::duration);
    }

    private List<CollabZone> findCollabZones() {
        return Pair.stream(prependLast(shiftChanges()))
                .filter(not(p -> p.left().members().isEmpty()))
                .map(this::toCollabZone)
                .toList();
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
        return toNormalPair(member).contains(time);
    }

    private TimePair toNormalPair(Member member) {
        return new TimePair(member.availability().normalStart(),
                member.availability().normalEnd());
    }

    private List<Time> shiftChangeTimes() {
        return normalTimes()
                .sorted(Comparator.comparing(Time::totalMinutesInUtc))
                .distinct()
                .toList();
    }

    private Stream<Time> normalTimes() {
        return members.stream()
                .map(Member::availability)
                .flatMap(a -> Stream.of(a.normalStart(), a.normalEnd()))
                .map(Time::inUtc);
    }

    private static <T> List<T> prependLast(List<T> list) {
        if(list.isEmpty()) {
            return List.of();
        }
        return Stream.concat(Stream.of(list.getLast()), list.stream()).toList();
    }
}
