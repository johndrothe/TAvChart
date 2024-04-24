package org.rothe.john.working_hours.model;

import lombok.Getter;
import lombok.Setter;

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
}
