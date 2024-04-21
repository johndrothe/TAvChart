package org.rothe.john.team_schedule.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Team {
    private final List<Member> members = new ArrayList<>();
    @Getter
    @Setter
    private String name;

    public Team(String name, List<Member> members) {
        this.name = name;
        this.members.addAll(members);
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public void addMember(Member member) {
        this.members.add(member);
    }

    public List<ZoneId> getZoneIds() {
        return Stream.concat(defaultZones(), getMemberZoneIds())
                .distinct().toList();
    }

    private static Stream<ZoneId> defaultZones() {
        return Stream.of(ZoneOffset.UTC);
    }

    private Stream<ZoneId> getMemberZoneIds() {
        return members.stream().map(Member::zoneId);
    }
}
