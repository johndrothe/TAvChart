package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Zone;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

// Algorithm Notes
// Goal: Find the longest contiguous overlaps (i.e. "shifts")
//
// if the longest contains all members, use it
// if the longest is missing members, then find the largest that includes those members

class ShiftTable {
    private static final int[] SHIFT_TIMES = shiftTimes();
    private final HashMap<Integer, Set<Member>> membersByShiftStart = new HashMap<>();
    private final List<Shift> shifts;
    private final List<Integer> shiftChanges;

    private ShiftTable(Collection<Member> members) {
        this.addMembers(members);
        this.shiftChanges = findShiftChanges();
        this.shifts = findShifts();
    }

    public static ShiftTable of(Collection<Member> members) {
        // TODO: Create a shift slot collector?
        return new ShiftTable(members);
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
        members.stream().flatMap(ShiftTable::toSlots).forEach(this::addShiftSlot);
    }

    private void addShiftSlot(ShiftSlot slot) {
        getShiftMembers(slot.minutesUtc).add(slot.member);
    }

    private Set<Member> getShiftMembers(int shiftTime) {
        return membersByShiftStart.computeIfAbsent(shiftTime, HashSet::new);
    }

    private List<Shift> findShifts() {
        val shifts = new ArrayList<Shift>();
        val wrappedChanges = new ArrayList<>(shiftChanges);
        wrappedChanges.add(wrappedChanges.getFirst());

        for (int i = 0; i < wrappedChanges.size() - 1; ++i) {
            int start = wrappedChanges.get(i);
            int end = wrappedChanges.get(i + 1);

            if (getShiftMembers(start).isEmpty()) {
                continue;
            }
            shifts.add(new Shift(start, end, getShiftMembers(start)));
        }
        return shifts;
    }

    private List<Integer> findShiftChanges() {
        val list = new ArrayList<Integer>();

        Set<Member> previousMembers = getShiftMembers(Zone.MINUTES_IN_A_DAY);
        for (int shiftTime : SHIFT_TIMES) {
            val currentMembers = getShiftMembers(shiftTime);
            if (Objects.equals(previousMembers, currentMembers)) {
                continue;
            }
            previousMembers = currentMembers;
            list.add(shiftTime);
        }
        return list;
    }

    private static Stream<ShiftSlot> toSlots(Member member) {
        val start = member.getNormalStartMinutesUtc();
        val end = member.getNormalEndMinutesUtc();

        return Arrays.stream(SHIFT_TIMES)
                .filter( minutesUtc -> minutesUtc >= start && minutesUtc < end)
                .mapToObj(minutesUtc -> new ShiftSlot(minutesUtc, member));
    }

    private static int[] shiftTimes() {
        int[] values = new int[24 * 4 + 1];

        for (int i = 0; i < values.length; ++i) {
            values[i] = i * 15;
        }
        return values;
    }

    private record ShiftSlot(int minutesUtc, Member member) {

    }
}
