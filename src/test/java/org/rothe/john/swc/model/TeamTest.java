package org.rothe.john.swc.model;

import org.junit.jupiter.api.Test;
import org.rothe.john.swc.io.json.Json;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeamTest {
    private final Member m1 = new Member("A", "AA", "AAA", Zone.utc());
    private final Member m2 = new Member("B", "BB", "BBB", Zone.utc());
    private final Member m3 = new Member("C", "CC", "CCC", Zone.utc());
    private final Member m4 = new Member("D", "DD", "DDD", Zone.utc());
    private final Member mX = new Member("X", "XX", "XXX", Zone.utc());

    @Test
    void testWithUpdatedMemberNone() {
        Team team = new Team("Name", List.of());

        assertThrows(IllegalArgumentException.class, () -> team.withUpdatedMember(m1, mX).members());
    }

    @Test
    void testWithUpdatedMemberOne() {
        Team team = new Team("Name", List.of(m1));

        assertEquals(List.of(mX), team.withUpdatedMember(m1, mX).members());
    }

    @Test
    void testWithUpdatedMemberTwo() {
        Team team = new Team("Name", List.of(m1, m2));

        assertEquals(List.of(mX, m2), team.withUpdatedMember(m1, mX).members());
        assertEquals(List.of(m1, mX), team.withUpdatedMember(m2, mX).members());
    }

    @Test
    void testWithUpdatedMemberThree() {
        Team team = new Team("Name", List.of(m1, m2, m3));

        assertEquals(List.of(mX, m2, m3), team.withUpdatedMember(m1, mX).members());
        assertEquals(List.of(m1, mX, m3), team.withUpdatedMember(m2, mX).members());
        assertEquals(List.of(m1, m2, mX), team.withUpdatedMember(m3, mX).members());
    }

    @Test
    void testWithUpdatedMemberFour() {
        Team team = new Team("Name", List.of(m1, m2, m3, m4));

        assertEquals(List.of(mX, m2, m3, m4), team.withUpdatedMember(m1, mX).members());
        assertEquals(List.of(m1, mX, m3, m4), team.withUpdatedMember(m2, mX).members());
        assertEquals(List.of(m1, m2, mX, m4), team.withUpdatedMember(m3, mX).members());
        assertEquals(List.of(m1, m2, m3, mX), team.withUpdatedMember(m4, mX).members());
    }

    @Test
    void testWithUpdatedWrongMember() {
        Team team = new Team("Name", List.of(m1, m2, m3));

        assertThrows(IllegalArgumentException.class, () -> team.withUpdatedMember(m4, mX).members());
    }

    @Test
    void testJsonEmpty() {
        Team empty = new Team("Name", List.of());
        assertEquals(empty, Json.fromJson(Json.toJson(empty), Team.class));
    }

    @Test
    void testJsonFour() {
        Team team = new Team("Name", List.of(m1, m2, m3, m4));
        assertEquals(team, Json.fromJson(Json.toJson(team), Team.class));
    }
}