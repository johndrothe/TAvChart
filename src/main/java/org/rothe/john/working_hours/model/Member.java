package org.rothe.john.working_hours.model;

import lombok.With;
import lombok.val;

import java.util.Collection;
import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

@With
public record Member(String name, String role, String location, Zone zone, Availability availability) {
    public Member(String name, String role, String location, Zone zone) {
        this(name, role, location, zone, Availability.businessHours(zone));
    }

    public String toCsv() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                name(), role(), location(), zone().getId(),
                availability().normalStart(),
                availability().normalEnd(),
                availability().lunchStart(),
                availability().lunchEnd());
    }

    public static Member fromCsv(String[] values) {
        val member = new Member(values[0], values[1], values[2], Zone.fromCsv(values[3]));
        return member.withAvailability(availability(member.zone(),
                values[4], values[5],
                values[6], values[7]));
    }

    private static Availability availability(Zone zone,
                                             String nStart, String nEnd,
                                             String lStart, String lEnd) {
        return new Availability(
                Time.parse(zone, nStart),
                Time.parse(zone, nEnd),
                Time.parse(zone, lStart),
                Time.parse(zone, lEnd)
        );
    }

    public static String toNameList(Collection<Member> members) {
        return members.stream()
                .map(Member::name)
                .sorted()
                .collect(joining(", "));
    }

    public static Comparator<Member> offsetNameCompartor() {
        final Comparator<Member> z = comparing(m -> m.zone().getOffsetHours());
        final Comparator<Member> n = comparing(Member::name);
        return z.thenComparing(n);
    }
}
