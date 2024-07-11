package org.rothe.john.working_hours.util;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PairTest {

    @Test
    void testLeftRight() {
        Pair<String> pair = new Pair<>("left", "right");
        assertEquals("left", pair.left());
        assertEquals("right", pair.right());
    }

    @Test
    void testStreamSimpleSets() {
        val list = List.of("a", "b", "c", "d", "e");
        assertEquals(Set.of("a", "b", "c", "d"), Pair.stream(list).map(Pair::left).collect(Collectors.toSet()));
        assertEquals(Set.of("b", "c", "d", "e"), Pair.stream(list).map(Pair::right).collect(Collectors.toSet()));
    }

    @Test
    void testPairStreamToList() {
        val expected = List.of(
                new Pair<>("a", "b"),
                new Pair<>("b", "c"),
                new Pair<>("c", "d"),
                new Pair<>("d", "e")
        );
        val result = Pair.stream(List.of("a", "b", "c", "d", "e")).toList();
        assertEquals(expected, result);
    }

}