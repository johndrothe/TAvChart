package org.rothe.john.working_hours.util;

import org.rothe.john.working_hours.model.*;

import java.time.ZoneId;
import java.util.List;

public abstract class SampleFactory {

    private SampleFactory() {

    }

    public static Team newTeam() {
        return new Team("Winners", demoMembers());
    }

    public static List<Member> debugMembers() {
        return List.of(
                toMember("Person #1", "UTC", "00:00-09:00", toZone("Z"), 0),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z")),
                toMember("Person #3", "NYC", "08:00-17:00", toZone("America/New_York")),
                toMember("Person #4", "NYC", "11:00-20:00", toZone("America/New_York"), 11),
                toMember("Person #5", "CHI", "08:00-17:00", toZone("America/Chicago")),
                toMember("Person #7", "PL", "08:00-17:00", toZone("Poland")),
                toMember("Person #9", "LAX", "08:00-17:00", toZone("America/Los_Angeles")),
                toMember("Person #10", "LAX", "10:00-19:00", toZone("America/Los_Angeles"), 10),
                toMember("Person #11", "LAX", "12:00-21:00", toZone("America/Los_Angeles"), 12)
        );
    }

    public static List<Member> debugShiftMembers() {
        return List.of(
                toMember("Person #1", "UTC", "00:00-09:00", toZone("Z"), 19),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z"), 20),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z"), 21),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z"), 22),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z"), 23),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z"), 20),
                toMember("Person #2", "UTC", "08:00-17:00", toZone("Z"), 21)
        );
    }

    public static List<Member> demoMembers() {
        return List.of(
                toMember("Great Dev", "Developer", "Jackson", toZone("America/Chicago")),
                toMember("Excellent Dev", "Developer", "Chicago", toZone("America/Chicago")),
                toMember("Product Manager", "PDM", "Baltimore", toZone("America/New_York")),
                toMember("Jill Lastname", "Director", "Berlin", toZone("Z")),
                toMember("Product Owner", "PO", "Berlin", toZone("Z")),
                toMember("Fantastic Dev", "Developer", "Warsaw", toZone("Poland")),
                toMember("Fabulous Dev", "Developer", "Prague", toZone("Poland"))
        );
    }

    private static Zone toZone(String zoneId) {
        return new Zone(ZoneId.of(zoneId));
    }

    private static Member toMember(String name, String role, String location, Zone zone) {
        return new Member(name, role, location, zone);
    }

    private static Member toMember(String name, String role, String location, Zone zone, int startHour) {
        return toMember(name, role, location, zone)
                .withNormal(officeNormal(zone, startHour))
                .withLunch(officeLunch(zone, startHour));
    }

    private static TimePair officeNormal(Zone zone, int startHour) {
        return new TimePair(Time.at(zone, startHour),
                Time.at(zone, Time.normalizeHour(startHour + 9)));
    }

    private static TimePair officeLunch(Zone zone, int startHour) {
        return new TimePair(Time.at(zone, Time.normalizeHour(startHour + 4)),
                Time.at(zone, Time.normalizeHour(startHour + 5)));
    }
}
