package org.rothe.john.swc.util;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PairTest {

    @Test
    void testLeftRight() {
        Pair<String> pair = Pair.of("left", "right");
        assertEquals("left", pair.left());
        assertEquals("right", pair.right());
    }

    @Test
    void testNullPairsThrowNPE() {
        assertThrows(NullPointerException.class, () -> Pair.of(null, "right"));
        assertThrows(NullPointerException.class, () -> Pair.of("left", null));
    }

    @Test
    void testPairStreamFromEmptyIsEmpty() {
        assertTrue(Pair.stream(List.of()).toList().isEmpty());
    }

    @Test
    void testPairStreamFromSingleEntryIsEmpty() {
        assertTrue(Pair.stream(List.of("a")).toList().isEmpty());
    }

    @Test
    void testPairStreamSimpleSets() {
        val list = List.of("a", "b", "c", "d", "e");
        assertEquals(Set.of("a", "b", "c", "d"), Pair.stream(list).map(Pair::left).collect(Collectors.toSet()));
        assertEquals(Set.of("b", "c", "d", "e"), Pair.stream(list).map(Pair::right).collect(Collectors.toSet()));
    }

    @Test
    void testOddLengthPairStreamToList() {
        val expected = List.of(
                Pair.of("a", "b"),
                Pair.of("b", "c"),
                Pair.of("c", "d"),
                Pair.of("d", "e")
        );
        val result = Pair.stream(List.of("a", "b", "c", "d", "e")).toList();
        assertEquals(expected, result);
    }

    @Test
    void testEvenLengthPairStreamToList() {
        val expected = List.of(
                Pair.of("a", "b"),
                Pair.of("b", "c"),
                Pair.of("c", "d")
        );
        val result = Pair.stream(List.of("a", "b", "c", "d")).toList();
        assertEquals(expected, result);
    }
}