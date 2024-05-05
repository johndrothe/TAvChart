package org.rothe.john.working_hours.event;

import org.rothe.john.working_hours.model.Team;

public record TeamChangedEvent(Object source, String event, Team oldTeam, Team team) {

}
