package work.rothe.tav.model;

import lombok.With;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

@With
public record Member(String name, String role, String location, Zone zone, TimePair normal, TimePair lunch) {
    public Member(String name, String role, String location, Zone zone) {
        this(name, role, location, zone, TimePair.businessNormal(zone), TimePair.businessLunch(zone));
    }

    public String toCsv() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                name(), role(), location(), zone().getId(),
                normal().left(), normal().right(),
                lunch().left(), lunch().right());
    }

    public static Member fromCsv(String[] values) {
        return fromCsv(values, Zone.fromCsv(values[3]));
    }

    private static Member fromCsv(String[] values, Zone zone) {
        return toMember(values[0], values[1], values[2], zone)
                .withNormal(toPair(zone, values[4], values[5]))
                .withLunch(toPair(zone, values[6], values[7]));
    }

    private static Member toMember(String name, String role, String location, Zone zone) {
        return new Member(name, role, location, zone);
    }

    private static TimePair toPair(Zone zone, String left, String right) {
        return new TimePair(Time.parse(zone, left), Time.parse(zone, right));
    }

    public static String toNameList(Collection<Member> members) {
        return members.stream()
                .map(Member::name)
                .sorted()
                .collect(joining(", "));
    }
}
