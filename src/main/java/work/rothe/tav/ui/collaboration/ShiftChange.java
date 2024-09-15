package work.rothe.tav.ui.collaboration;

import work.rothe.tav.model.Member;
import work.rothe.tav.model.Time;

import java.util.List;

public record ShiftChange(List<Member> members, Time time) {
    public boolean hasMembers() {
        return !members.isEmpty();
    }
}
