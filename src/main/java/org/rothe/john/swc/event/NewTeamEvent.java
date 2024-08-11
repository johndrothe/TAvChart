package org.rothe.john.swc.event;

import org.rothe.john.swc.model.Team;

public class NewTeamEvent extends TeamChangedEvent {

    public NewTeamEvent(Object source, String event, Team team) {
        super(source, event, null, team);
    }
}
