package org.rothe.john.working_hours.util;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

public record Pair<T>(T left, T right) {
    public Pair {
        requireNonNull(left);
        requireNonNull(right);
    }

    public static <T> Pair<T> of(T left, T right) {
        return new Pair<>(left, right);
    }

    public static <T> Stream<Pair<T>> stream(List<T> list) {
        return StreamSupport.stream(PairingIterator.splitOf(list), false);
    }
}
