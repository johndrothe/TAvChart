package org.rothe.john.working_hours.util;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;

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
                new Member("Product Owner", "PO", "Berlin", toZone("Z")),
                new Member("Product Manager", "PDM", "Baltimore", toZone("America/New_York")),
                new Member("Super Dev", "Developer", "Manhattan", toZone("America/New_York")),
                new Member("Great Dev", "Developer", "Jackson", toZone("America/Chicago")),
                new Member("Excellent Dev", "Developer", "Chicago", toZone("America/Chicago")),
                new Member("Fantastic Dev", "Developer", "Warsaw", toZone("Poland")),
                new Member("Fabulous Dev", "Developer", "Prague", toZone("Poland"))
//                        new Member("Jill Lastname", "Developer", "Los Angeles", toZone("America/Los_Angeles")),
//                        new Member("Jane Smith", "Developer", "San Francisco", toZone("America/Los_Angeles"))
        );
    }
    
    private static Zone toZone(String zoneId) {
        return new Zone(ZoneId.of(zoneId));
    }
}
