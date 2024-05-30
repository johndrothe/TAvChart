package org.rothe.john.working_hours.ui.canvas.collaboration.calculator;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

import java.util.*;
import java.util.stream.Stream;

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
        val changes = new ArrayList<>(shiftChanges());
        if (changes.isEmpty()) {
            return List.of();
        }

        List<CollabZone> list = new ArrayList<>();
        val it = PairingIterator.of(changes);
        while (it.hasNext()) {
            val pair = it.next();
            if (pair.left().members().isEmpty()) {
                continue;
            }
            list.add(toCollabZone(pair));
        }
        return Collections.unmodifiableList(list);
    }

    private CollabZone toCollabZone(PairingIterator.Pair<ShiftChange> pair) {
        return new CollabZone(toTimePair(pair), Set.copyOf(pair.left().members()));
    }

    private TimePair toTimePair(PairingIterator.Pair<ShiftChange> pair) {
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
}
