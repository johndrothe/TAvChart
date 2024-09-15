package work.rothe.tav.util;

import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.model.Time;
import work.rothe.tav.model.TimePair;
import work.rothe.tav.model.Zone;

import java.time.ZoneId;
import java.util.List;

public abstract class SampleFactory {

    private SampleFactory() {

    }

    public static Document newDocument() {
        return new Document("Winners", 0, demoMembers());
    }

    public static List<Member> debugMembers() {
        return List.of(
                toMember("AAAAA AAAAAAAAA", "UTC", "00:00-09:00", toZone("Z"), 0),
                toMember("BBBB BBBBBBBBBB", "UTC", "08:00-17:00", toZone("Z")),
                toMember("CCCCCCC CCCCCCC", "NYC", "08:00-17:00", toZone("America/New_York")),
                toMember("DDDDD DDDDDDDDD", "NYC", "11:00-20:00", toZone("America/New_York"), 11),
                toMember("EEE EEEEEEEEEEE", "CHI", "08:00-17:00", toZone("America/Chicago")),
                toMember("FF FFFFFFFFFFFF", "PL", "08:00-17:00", toZone("Poland")),
                toMember("GG GGGGGGGGGGGG", "LAX", "08:00-17:00", toZone("America/Los_Angeles")),
                toMember("HHHHH HHHHHHHHH", "LAX", "10:00-19:00", toZone("America/Los_Angeles"), 10),
                toMember("JJJJ JJJJJJJJJJ", "LAX", "12:00-21:00", toZone("America/Los_Angeles"), 12)
        );
    }

    public static List<Member> centeringDebugMembers() {
        return List.of(
                toMember("AAAAA AAAAAAAAA", "UTC", "00:00-09:00", toZone("Z"), 13),
                toMember("BBBB BBBBBBBBBB", "UTC", "08:00-17:00", toZone("Z"), 13),
                toMember("CCCCCCC CCCCCCC", "NYC", "08:00-17:00", toZone("America/New_York"), 14),
                toMember("DDDDD DDDDDDDDD", "NYC", "11:00-20:00", toZone("America/New_York"), 16),
                toMember("EEE EEEEEEEEEEE", "CHI", "08:00-17:00", toZone("America/Chicago"), 16),
                toMember("FF FFFFFFFFFFFF", "PL", "08:00-17:00", toZone("Poland"), 16),
                toMember("GG GGGGGGGGGGGG", "LAX", "08:00-17:00", toZone("America/Los_Angeles"),18),
                toMember("HHHHH HHHHHHHHH", "LAX", "10:00-19:00", toZone("America/Los_Angeles"), 19),
                toMember("JJJJ JJJJJJJJJJ", "LAX", "12:00-21:00", toZone("America/Los_Angeles"), 19)
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
