package org.rothe.john.working_hours.ui.collaboration;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Time;

import java.util.List;

public record ShiftChange(List<Member> members, Time time) {
    public boolean hasMembers() {
        return !members.isEmpty();
    }
}
