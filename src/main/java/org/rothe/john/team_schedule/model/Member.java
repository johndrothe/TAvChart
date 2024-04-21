package org.rothe.john.team_schedule.model;

import java.time.ZoneId;

public record Member(String name, String position, String location, ZoneId zoneId, Availability availability) {
    public Member(String name, String position, String location, ZoneId zoneId){
        this(name, position, location, zoneId, Availability.standard());
    }
}
