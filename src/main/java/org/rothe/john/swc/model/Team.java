package org.rothe.john.swc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@With
public record Team(String name, List<Member> members) {
    @JsonCreator
    public Team(@JsonProperty("name") String name,
                @JsonProperty("members") List<Member> members) {
        this.name = name;
        this.members = List.copyOf(members);
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

    @JsonIgnore
    public List<Zone> zones() {
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
        return members().stream()
                .map(Member::toCsv)
                .collect(joining("\n", csvHeader(), "\n"));
    }

    public Team withUpdatedMember(Member oldMember, Member newMember) {
        return withMembers(replaceMember(new ArrayList<>(members), oldMember, newMember));
    }

    private static List<Member> replaceMember(List<Member> list, Member oldMember, Member newMember) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == oldMember) {
                list.set(i, newMember);
                return List.copyOf(list);
            }
        }
        throw new IllegalArgumentException("No such member found in member list.");
    }

    private String csvHeader() {
        return "Name, Role, Location, Zone ID, Normal Start, Normal End, Lunch Start, Lunch End\n";
    }
}