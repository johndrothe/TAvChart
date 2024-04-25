package org.rothe.john.working_hours.model;

public record Member(String name, String position, String location, Zone zone, Availability availability) {
    public Member(String name, String position, String location, Zone zone){
        this(name, position, location, zone, Availability.standard());
    }

    public String toCsv() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                name(), position(), location(), zone().getId(),
                availability().normal().start(),
                availability().normal().end(),
                availability().lunch().start(),
                availability().lunch().end());
    }

    ///public static Member from(List<String)
}
