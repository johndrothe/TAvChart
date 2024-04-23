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
                new Member("Gertrude Bauer", "PO", "Berlin", toZone("Z")),
                new Member("Trevor Jones", "PDM", "Baltimore", toZone("America/New_York")),
                new Member("Bob Hope", "Developer", "Manhattan", toZone("America/New_York")),
                new Member("Miles Davis", "Developer", "Jackson", toZone("America/Chicago")),
                new Member("Davis Lynn", "Developer", "Chicago", toZone("America/Chicago")),
                new Member("Tomasz Chlebek", "Developer", "Warsaw", toZone("Poland")),
                new Member("Danuta Adamski", "Developer", "Prague", toZone("Poland"))
//                        new Member("Jill Lastname", "Developer", "Los Angeles", toZone("America/Los_Angeles")),
//                        new Member("Jane Smith", "Developer", "San Francisco", toZone("America/Los_Angeles"))
        );
    }
    
    private static Zone toZone(String zoneId) {
        return new Zone(ZoneId.of(zoneId));
    }
}
