package org.rothe.john.working_hours.ui.canvas.shifts.calculator;

import lombok.val;

import java.util.Iterator;
import java.util.List;

class PairingIterator<T> {
    private final Iterator<T> it;
    private T left = null;

    private PairingIterator(List<T> list) {
        this.it = List.copyOf(list).iterator();
        if (!list.isEmpty()) {
            this.left = list.getLast();
        }
    }

    public static <T> PairingIterator<T> of(List<T> list) {
        return new PairingIterator<>(list);
    }

    public Pair<T> next() {
        val pair = new Pair<>(left, it.next());
        left = pair.right;
        return pair;
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public static class Pair<T> {
        private final T left;
        private final T right;

        private Pair(T left, T right) {
            this.left = left;
            this.right = right;
        }

        public T left() {
            return left;
        }

        public T right() {
            return right;
        }
    }
}
