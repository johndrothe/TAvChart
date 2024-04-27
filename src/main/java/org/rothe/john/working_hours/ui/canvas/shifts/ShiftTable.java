package org.rothe.john.working_hours.ui.canvas.shifts;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Zone;

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

import static java.util.Objects.isNull;

class ShiftTable {
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
        // find the longest contiguous overlap (i.e. "shift")
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
        addShiftSlots(members.stream().flatMap(ShiftTable::toSlots).toList());
    }

    private void addShiftSlots(List<ShiftSlot> slots) {
        for (ShiftSlot slot : slots) {
            membersByShiftStart.computeIfAbsent(slot.minutesUtc, HashSet::new).add(slot.member);
        }
    }

    private List<Shift> findShifts() {
        val shifts = new ArrayList<Shift>();
        val wrappedChanges = new ArrayList<>(shiftChanges);
        wrappedChanges.add(wrappedChanges.getFirst());

        for (int i = 0; i < wrappedChanges.size() - 1; ++i) {
            int start = wrappedChanges.get(i);
            int end = wrappedChanges.get(i + 1);

            if (isNull(membersByShiftStart.get(start)) || membersByShiftStart.get(start).isEmpty()) {
                continue;
            }
            shifts.add(new Shift(start, end, membersByShiftStart.get(start)));
        }
        return shifts;
    }

    private List<Integer> findShiftChanges() {
        val list = new ArrayList<Integer>();

        Set<Member> previousMembers = membersByShiftStart.get(Zone.MINUTES_IN_A_DAY);
        for (int minutesUtc = 0; minutesUtc <= Zone.MINUTES_IN_A_DAY; minutesUtc += 15) {
            val currentMembers = membersByShiftStart.get(minutesUtc);
            if (Objects.equals(previousMembers, currentMembers)) {
                continue;
            }
            previousMembers = currentMembers;
            list.add(minutesUtc);
        }
        return list;
    }

    private static Stream<ShiftSlot> toSlots(Member member) {
        val start = member.getNormalStartMinutesUtc();
        val end = member.getNormalEndMinutesUtc();

        val list = new ArrayList<ShiftSlot>();
        for (int minutesUtc = 0; minutesUtc < Zone.MINUTES_IN_A_DAY; minutesUtc += 15) {
            if (minutesUtc >= start && minutesUtc < end) {
                list.add(new ShiftSlot(minutesUtc, member));
            }
        }
        return list.stream();
    }

    private record ShiftSlot(int minutesUtc, Member member) {

    }
}
