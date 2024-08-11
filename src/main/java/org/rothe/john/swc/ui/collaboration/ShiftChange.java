package org.rothe.john.swc.ui.collaboration;

import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.model.Time;

import java.util.List;

public record ShiftChange(List<Member> members, Time time) {
    public boolean hasMembers() {
        return !members.isEmpty();
    }
}
