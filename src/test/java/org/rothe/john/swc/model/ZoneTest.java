package org.rothe.john.swc.model;

import org.junit.jupiter.api.Test;
import org.rothe.john.swc.io.json.Json;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZoneTest {
    private final Zone NYC = Zone.fromCsv("America/New_York");
    private final Zone CHI = Zone.fromCsv("America/Chicago");
    private final Zone LAX = Zone.fromCsv("America/Los_Angeles");
    private final Zone PL = Zone.fromCsv("Poland");

    @Test
    void testUtc() {
        assertEquals(new Zone(ZoneOffset.UTC), Zone.utc());
        assertEquals(Zone.utc(), Zone.fromCsv("UTC"));
    }

    @Test
    void testHasTransitions() {
        assertFalse(Zone.utc().hasTransitions());
        assertTrue(NYC.hasTransitions());
        assertTrue(CHI.hasTransitions());
        assertTrue(LAX.hasTransitions());
        assertTrue(PL.hasTransitions());
    }

    @Test
    void testFromCsv() {
        assertEquals(Zone.utc(), Zone.fromCsv(ZoneOffset.UTC.getId()));
        assertEquals(new Zone(ZoneId.of("America/New_York")), NYC);
        assertEquals(new Zone(ZoneId.of("America/Chicago")), CHI);
        assertEquals(new Zone(ZoneId.of("America/Los_Angeles")), LAX);
        assertEquals(new Zone(ZoneId.of("Poland")), PL);
    }

    @Test
    void testZoneToJson() {
        assertEquals("{\n  \"id\" : \"UTC\"\n}", Json.toJson(Zone.utc()));
        assertEquals("{\n  \"id\" : \"America/New_York\"\n}", Json.toJson(NYC));
        assertEquals("{\n  \"id\" : \"America/Chicago\"\n}", Json.toJson(CHI));
        assertEquals("{\n  \"id\" : \"America/Los_Angeles\"\n}", Json.toJson(LAX));
        assertEquals("{\n  \"id\" : \"Poland\"\n}", Json.toJson(PL));
    }

    @Test
    void testZoneFromJson() {
        assertEquals(Zone.utc(), Json.fromJson("{\n  \"id\" : \"UTC\"\n}", Zone.class));
        assertEquals(NYC, Json.fromJson("{\n  \"id\" : \"America/New_York\"\n}", Zone.class));
        assertEquals(CHI, Json.fromJson("{\n  \"id\" : \"America/Chicago\"\n}", Zone.class));
        assertEquals(LAX, Json.fromJson("{\n  \"id\" : \"America/Los_Angeles\"\n}", Zone.class));
        assertEquals(PL, Json.fromJson("{\n  \"id\" : \"Poland\"\n}", Zone.class));
    }

}