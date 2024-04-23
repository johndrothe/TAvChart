package org.rothe.john.working_hours.model;

import org.rothe.john.working_hours.util.Zone;

public record Member(String name, String position, String location, Zone zone, Availability availability) {
    public Member(String name, String position, String location, Zone zone){
        this(name, position, location, zone, Availability.standard());
    }
}
