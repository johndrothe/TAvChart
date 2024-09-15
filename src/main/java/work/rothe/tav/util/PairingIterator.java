package work.rothe.tav.util;

import lombok.val;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SIZED;

public class PairingIterator<T> implements Iterator<Pair<T>> {
    private static final int CHARACTERISTICS = SIZED | IMMUTABLE | NONNULL | ORDERED;
    private final Iterator<T> it;
    private T nextLeft = null;

    private PairingIterator(List<T> list) {
        this.it = List.copyOf(list).iterator();
        if (it.hasNext()) {
            this.nextLeft = it.next();
        }
    }

    public static <T> PairingIterator<T> of(List<T> list) {
        return new PairingIterator<>(list);
    }

    public static <T> Spliterator<Pair<T>> splitOf(List<T> list) {
        return Spliterators.spliterator(of(list), Math.max(0, list.size() - 1), CHARACTERISTICS);
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
