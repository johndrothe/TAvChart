package org.rothe.john.working_hours.model;

import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@With
public class Team {
    @Getter
    @Setter
    private String name;
    private List<Member> members;

    public Team(String name, List<Member> members) {
        this.name = name;
        this.members = new ArrayList<>(members);
    }

    public static Team fromCsv(String fileName, String csvContents) {
        if (csvContents.isEmpty()) {
            return null;
        }
        return new Team(toTeamName(fileName), membersFromCsv(csvContents));
    }

    private static List<Member> membersFromCsv(String csvContents) {
        return Arrays.stream(csvContents.split("\n"))
                .skip(1)//column headers
                .map(row -> row.split(", ?"))
                .map(Member::fromCsv)
                .toList();
    }

    private static String toTeamName(String fileName) {
        if (fileName.toLowerCase().endsWith(".csv")) {
            return fileName.substring(0, fileName.length() - 4);
        }
        return fileName;
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<Zone> getZones() {
        return Stream.concat(defaultZones(), getMemberZoneIds())
                .distinct().toList();
    }

    private static Stream<Zone> defaultZones() {
        return Stream.of(new Zone(ZoneOffset.UTC));
    }

    private Stream<Zone> getMemberZoneIds() {
        return members.stream().map(Member::zone);
    }

    public String toCsv() {
        return getMembers().stream()
                .map(Member::toCsv)
                .collect(joining("\n", csvHeader(), "\n"));
    }

    private String csvHeader() {
        return "Name, Role, Location, Zone ID, Normal Start, Normal End, Lunch Start, Lunch End\n";
    }
}
