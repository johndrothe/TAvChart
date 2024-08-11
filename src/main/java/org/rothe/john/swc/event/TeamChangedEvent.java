package org.rothe.john.swc.event;

import lombok.RequiredArgsConstructor;
import org.rothe.john.swc.model.Team;

@RequiredArgsConstructor
public class TeamChangedEvent {
    private final Object source;
    private final String event;
    private final Team oldTeam;
    private final Team team;

    public Object source() {
        return source;
    }

    public String event() {
        return event;
    }

    public Team oldTeam() {
        return oldTeam;
    }

    public Team team() {
        return team;
    }
}
