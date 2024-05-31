package org.rothe.john.working_hours.ui.util;

import lombok.val;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Pair<T> {
    private final T left;
    private final T right;

    Pair(T left, T right) {
        this.left = left;
        this.right = right;
    }

    public static <T> Stream<Pair<T>> asPairStream(List<T> list) {
        Iterable<Pair<T>> iterable = () -> new PairingIterator<>(list);
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public T left() {
        return left;
    }

    public T right() {
        return right;
    }

    private static class PairingIterator<T> implements Iterator<Pair<T>> {
        private final Iterator<T> it;
        private T left = null;

        PairingIterator(List<T> list) {
            this.it = List.copyOf(list).iterator();
            if (!list.isEmpty()) {
                this.left = list.getLast();
            }
        }

        public Pair<T> next() {
            val pair = new Pair<>(left, it.next());
            left = pair.right();
            return pair;
        }

        public boolean hasNext() {
            return it.hasNext();
        }
    }
}
