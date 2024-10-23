package work.rothe.tav.ui.collaboration;

import lombok.val;
import org.junit.jupiter.api.Test;
import work.rothe.tav.model.Member;
import work.rothe.tav.model.Time;
import work.rothe.tav.model.TimePair;
import work.rothe.tav.model.Zone;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static work.rothe.tav.ui.collaboration.CollabZone.comparator;


public class CollabZoneTest {
    private final Member M1 = toMember("M1", toZone("America/Chicago"));
    private final Member M2 = toMember("M2", toZone("America/Chicago"));
    private final Member M3 = toMember("M3", toZone("America/New_York"));
    private final Member M4 = toMember("M4", toZone("UTC"));
    private final Member M5 = toMember("M5", toZone("UTC"));
    private final Member M6 = toMember("M6", toZone("Poland"));
    private final Member M7 = toMember("M7", toZone("Poland"));

    private final CollabZone Z1 = new CollabZone(utcPair(13, 15), Set.of(M1, M2, M3, M4, M5, M6, M7));
    private final CollabZone Z2 = new CollabZone(utcPair(15, 17), Set.of(M1, M2, M3, M4, M5));
    private final CollabZone Z3 = new CollabZone(utcPair(12, 13), Set.of(M3, M4, M5, M6, M7));
    private final CollabZone Z4 = new CollabZone(utcPair(8, 12), Set.of(M4, M5, M6, M7));
    private final CollabZone Z5 = new CollabZone(utcPair(17, 21), Set.of(M1, M2, M3));
    private final CollabZone Z6 = new CollabZone(utcPair(6, 8), Set.of(M6, M7));
    private final CollabZone Z7 = new CollabZone(utcPair(21, 22), Set.of(M1, M2));


    @Test
    void testComparator() {
        val expected = List.of(Z7, Z6, Z5, Z4, Z3, Z2, Z1);
        val result = Stream.of(Z7, Z2, Z6, Z4, Z3, Z1, Z5)
                .sorted(comparator())
                .toList();

        assertEquals(expected, result);
    }

    private static Zone toZone(String zoneId) {
        return new Zone(ZoneId.of(zoneId));
    }

    private static Member toMember(String name, Zone zone) {
        return new Member(name, "", "", zone);
    }

    private static TimePair utcPair(int hour1, int hour2) {
        return new TimePair(utcTime(hour1), utcTime(hour2));
    }

    private static Time utcTime(int hour) {
        return Time.at(Zone.utc(), hour, 0);
    }
}
