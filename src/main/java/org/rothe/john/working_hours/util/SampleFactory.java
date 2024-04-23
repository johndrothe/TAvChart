package org.rothe.john.working_hours.util;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;

import java.time.ZoneId;
import java.util.List;

public abstract class SampleFactory {

    private SampleFactory() {

    }

    public static Team newTeam() {
        return new Team("Winners", newMembers());
    }

    public static List<Member> newMembers() {
        return List.of(
                new Member("Gertrude Bauer", "PO", "Berlin", ZoneId.of("Z")),
                new Member("Trevor Jones", "PDM", "Baltimore", ZoneId.of("America/New_York")),
                new Member("Bob Hope", "Developer", "Manhattan", ZoneId.of("America/New_York")),
                new Member("Miles Davis", "Developer", "Jackson", ZoneId.of("America/Chicago")),
                new Member("Davis Lynn", "Developer", "Chicago", ZoneId.of("America/Chicago")),
                new Member("Tomasz Chlebek", "Developer", "Warsaw", ZoneId.of("Poland")),
                new Member("Danuta Adamski", "Developer", "Prague", ZoneId.of("Poland"))
//                        new Member("Jill Lastname", "Developer", "Los Angeles", ZoneId.of("America/Los_Angeles")),
//                        new Member("Jane Smith", "Developer", "San Francisco", ZoneId.of("America/Los_Angeles"))
        );
    }
}
