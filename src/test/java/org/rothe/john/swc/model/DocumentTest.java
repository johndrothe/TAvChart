package org.rothe.john.swc.model;

import org.junit.jupiter.api.Test;
import org.rothe.john.swc.io.json.Json;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DocumentTest {
    private static final int BORDER_HOUR = 0;
    private final Member m1 = new Member("A", "AA", "AAA", Zone.utc());
    private final Member m2 = new Member("B", "BB", "BBB", Zone.utc());
    private final Member m3 = new Member("C", "CC", "CCC", Zone.utc());
    private final Member m4 = new Member("D", "DD", "DDD", Zone.utc());
    private final Member mX = new Member("X", "XX", "XXX", Zone.utc());

    @Test
    void testWithUpdatedMemberNone() {
        Document document = new Document("Name", BORDER_HOUR, List.of());

        assertThrows(IllegalArgumentException.class, () -> document.withUpdatedMember(m1, mX).members());
    }

    @Test
    void testWithUpdatedMemberOne() {
        Document document = new Document("Name", BORDER_HOUR, List.of(m1));

        assertEquals(List.of(mX), document.withUpdatedMember(m1, mX).members());
    }

    @Test
    void testWithUpdatedMemberTwo() {
        Document document = new Document("Name", BORDER_HOUR, List.of(m1, m2));

        assertEquals(List.of(mX, m2), document.withUpdatedMember(m1, mX).members());
        assertEquals(List.of(m1, mX), document.withUpdatedMember(m2, mX).members());
    }

    @Test
    void testWithUpdatedMemberThree() {
        Document document = new Document("Name", BORDER_HOUR, List.of(m1, m2, m3));

        assertEquals(List.of(mX, m2, m3), document.withUpdatedMember(m1, mX).members());
        assertEquals(List.of(m1, mX, m3), document.withUpdatedMember(m2, mX).members());
        assertEquals(List.of(m1, m2, mX), document.withUpdatedMember(m3, mX).members());
    }

    @Test
    void testWithUpdatedMemberFour() {
        Document document = new Document("Name", BORDER_HOUR, List.of(m1, m2, m3, m4));

        assertEquals(List.of(mX, m2, m3, m4), document.withUpdatedMember(m1, mX).members());
        assertEquals(List.of(m1, mX, m3, m4), document.withUpdatedMember(m2, mX).members());
        assertEquals(List.of(m1, m2, mX, m4), document.withUpdatedMember(m3, mX).members());
        assertEquals(List.of(m1, m2, m3, mX), document.withUpdatedMember(m4, mX).members());
    }

    @Test
    void testWithUpdatedWrongMember() {
        Document document = new Document("Name", BORDER_HOUR, List.of(m1, m2, m3));

        assertThrows(IllegalArgumentException.class, () -> document.withUpdatedMember(m4, mX).members());
    }

    @Test
    void testJsonEmpty() {
        Document empty = new Document("Name", BORDER_HOUR, List.of());
        assertEquals(empty, Json.fromJson(Json.toJson(empty), Document.class));
    }

    @Test
    void testJsonFour() {
        Document document = new Document("Name", BORDER_HOUR, List.of(m1, m2, m3, m4));
        assertEquals(document, Json.fromJson(Json.toJson(document), Document.class));
    }
}