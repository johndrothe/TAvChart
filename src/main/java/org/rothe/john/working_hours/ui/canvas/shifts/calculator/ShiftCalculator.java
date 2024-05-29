package org.rothe.john.working_hours.ui.canvas.shifts.calculator;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;
import org.rothe.john.working_hours.util.SampleFactory;

import java.util.*;
import java.util.stream.Stream;

public class ShiftCalculator {
    private final List<Member> members;

    private ShiftCalculator(Collection<Member> members) {
        this.members = members.stream().toList();
    }

    public static ShiftCalculator of(Collection<Member> members) {
        return new ShiftCalculator(members);
    }

    public static void main(String[] args) {
        ShiftCalculator calculator = new ShiftCalculator(SampleFactory.debugMembers());
        System.err.println("<changes>");
        for (ShiftChange c : calculator.shiftChanges()) {
            System.err.printf("    %s (%s) - %s%n", c.time(), c.time().zone(), c.members().stream().map(Member::name).toList());
        }
        System.err.println("</changes>");


        System.err.println(Shift.toListString(calculator.findShifts()));
    }

    public List<Shift> largestShift() {
        return findShifts().stream()
                .sorted(Comparator.comparing(Shift::size).reversed().thenComparing(Shift::duration))
                .limit(1)
                .toList();
    }

    private List<Shift> findShifts() {
        val changes = new ArrayList<>(shiftChanges());
        if (changes.isEmpty()) {
            return List.of();
        }

        List<Shift> list = new ArrayList<>();
        val it = PairingIterator.of(changes);
        while (it.hasNext()) {
            val pair = it.next();
            if (pair.left().members().isEmpty()) {
                continue;
            }
            list.add(toShift(pair));
        }
        return Collections.unmodifiableList(list);
    }

    private Shift toShift(PairingIterator.Pair<ShiftChange> pair) {
        return new Shift(toTimePair(pair), Set.copyOf(pair.left().members()));
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
