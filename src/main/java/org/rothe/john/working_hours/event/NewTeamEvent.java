package org.rothe.john.working_hours.event;

import org.rothe.john.working_hours.model.Team;

public class NewTeamEvent extends TeamChangedEvent {

    public NewTeamEvent(Object source, String event, Team team) {
        super(source, event, null, team);
    }
}
