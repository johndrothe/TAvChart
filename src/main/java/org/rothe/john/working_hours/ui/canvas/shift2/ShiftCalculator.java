package org.rothe.john.working_hours.ui.canvas.shift2;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;
import org.rothe.john.working_hours.util.SampleFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ShiftCalculator {
    private final List<Member> members;
    private final SpaceTime spaceTime;

    private ShiftCalculator(Collection<Member> members, SpaceTime spaceTime) {
        this.members = members.stream().toList();
        this.spaceTime = spaceTime;
    }

    public static ShiftCalculator of(Collection<Member> members, SpaceTime spaceTime) {
        return new ShiftCalculator(members, spaceTime);
    }

    public static void main(String[] args) {
        ShiftCalculator calculator = new ShiftCalculator(SampleFactory.demoMembers(), null);
        System.err.println("<changes>");
        for(ShiftChange c : calculator.shiftChanges()) {
            System.err.printf("    %s (%s) - %s%n", c.time(), c.time().zone(), c.members().stream().map(Member::name).toList());
        }
        System.err.println("</changes>");
    }

    public List<Shift2> shifts() {
        return List.of();
    }

    public List<ShiftChange> shiftChanges() {
        return shiftChangeTimes().stream()
                .peek(System.err::println)
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
