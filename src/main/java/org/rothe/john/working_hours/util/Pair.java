package org.rothe.john.working_hours.util;

import lombok.EqualsAndHashCode;
import lombok.val;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SIZED;

@EqualsAndHashCode
public class Pair<T> {
    private final T left;
    private final T right;

    private Pair(T left, T right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    public static <T> Pair<T> of(T left, T right) {
        return new Pair<>(left, right);
    }

    public T left() {
        return left;
    }

    public T right() {
        return right;
    }

    public static <T> Stream<Pair<T>> stream(List<T> list) {
        return StreamSupport.stream(spliterator(list), false);
    }

    @Override
    public String toString() {
        return String.format("Pair(%s, %s)", left, right);
    }

    private static <T> Spliterator<Pair<T>> spliterator(List<T> list) {
        return Spliterators.spliterator(new PairingIterator<>(list),
                Math.max(0, list.size() - 1), SIZED | IMMUTABLE | NONNULL | ORDERED);
    }

    private static class PairingIterator<T> implements Iterator<Pair<T>> {
        private final Iterator<T> it;
        private T nextLeft = null;

        PairingIterator(List<T> list) {
            this.it = List.copyOf(list).iterator();
            if (it.hasNext()) {
                this.nextLeft = it.next();
            }
        }

        @Override
        public Pair<T> next() {
            val pair = Pair.of(nextLeft, it.next());
            nextLeft = pair.right();
            return pair;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
    }
}
