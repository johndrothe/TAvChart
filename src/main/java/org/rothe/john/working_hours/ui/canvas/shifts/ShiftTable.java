package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.val;
import org.rothe.john.working_hours.model.Availability;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.ui.canvas.st.SpaceTime;
import org.rothe.john.working_hours.ui.canvas.st.TimePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

// Algorithm Notes
// Goal: Find the longest contiguous overlaps (i.e. "shifts")
//
// if the longest contains all members, use it
// if the longest is missing members, then find the largest that includes those members

class ShiftTable {
    private final HashMap<Integer, Set<Member>> membersByShiftStart = new HashMap<>();
    private final SpaceTime spaceTime;
    private final List<Shift> shifts;
    private final List<Integer> shiftChanges;

    private ShiftTable(Collection<Member> members, SpaceTime spaceTime) {
        this.spaceTime = spaceTime;
        this.addMembers(members);
        this.shiftChanges = findShiftChanges();
        this.shifts = findShifts();
    }

    public static ShiftTable of(Collection<Member> members, SpaceTime spaceTime) {
        // TODO: Create a shift slot collector?
        return new ShiftTable(members, spaceTime);
    }

    public List<Shift> getLargestShifts() {
        return getShiftsBySize(getMaxShiftSize());
    }

    public List<Integer> getShiftChanges() {
        return Collections.unmodifiableList(shiftChanges);
    }

    private List<Shift> getShiftsBySize(int size) {
        return shifts.stream()
                .filter(s -> s.size() == size)
                .sorted(Comparator.comparing(Shift::duration))
                .toList();
    }

    private int getMaxShiftSize() {
        return shifts.stream()
                .mapToInt(Shift::size)
                .max()
                .orElse(0);
    }

    private void addMembers(Collection<Member> members) {
        members.stream().flatMap(this::toSlots).forEach(this::addShiftSlot);
    }

    private void addShiftSlot(ShiftSlot slot) {
        getShiftMembers(slot.minutesUtc).add(slot.member);
    }

    private Set<Member> getShiftMembers(int shiftTime) {
        return membersByShiftStart.computeIfAbsent(shiftTime, HashSet::new);
    }

    private List<Shift> findShifts() {
        val shifts = new ArrayList<Shift>();
        val changeTimes = getWrapAroundShiftChanges();

        for (int i = 0; i < changeTimes.size() - 1; ++i) {
            int start = changeTimes.get(i);
            int end = changeTimes.get(i + 1);

            if (getShiftMembers(start).isEmpty()) {
                continue;
            }
            shifts.add(new Shift(start, end, getShiftMembers(start)));
        }
        return shifts;
    }

    // the list of shift changes with the first at the beginning and
    // the end of the list
    private List<Integer> getWrapAroundShiftChanges() {
        if (shiftChanges.isEmpty()) {
            return List.of();
        }
        return Stream.concat(shiftChanges.stream(), Stream.of(shiftChanges.getFirst()))
                .toList();
    }

    private List<Integer> findShiftChanges() {
        val list = new ArrayList<Integer>();

        Set<Member> previousMembers = getShiftMembers(Time.MINUTES_IN_A_DAY);
        for (int shiftTime : ShiftTimes.get()) {
            val currentMembers = getShiftMembers(shiftTime);
            if (Objects.equals(previousMembers, currentMembers)) {
                continue;
            }

//            printShiftChange(shiftTime, previousMembers, currentMembers);
            previousMembers = currentMembers;
            list.add(shiftTime);
        }
        return list;
    }

    private static void printShiftChange(int shiftTime,
                                         Set<Member> previousMembers,
                                         Set<Member> currentMembers) {
        System.err.printf("Shift change at %s  - %s --> %s %n",
                Time.toClockFormat(shiftTime),
                previousMembers.stream().map(Member::name).toList(),
                currentMembers.stream().map(Member::name).toList()
        );
    }

    private Stream<ShiftSlot> toSlots(Member member) {
        return spaceTime.splitAroundBorder(normal(member.availability())).stream()
                .flatMap(pair -> toSlots(member, pair.left(), pair.right()));
    }

    private static Stream<ShiftSlot> toSlots(Member member, Time startTime, Time endTime) {
        val start = startTime.totalMinutesInUtc();
        val end = endTime.totalMinutesInUtc();

        return ShiftTimes.stream()
                .filter(minutesUtc -> minutesUtc >= start && minutesUtc < end)
                .mapToObj(minutesUtc -> new ShiftSlot(minutesUtc, member));
    }

    private TimePair normal(Availability availability) {
        return new TimePair(availability.normalStart(), availability.normalEnd());
    }

    private record ShiftSlot(int minutesUtc, Member member) {

    }
}
