package org.rothe.john.swc.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ZoomHandler {
    private final Supplier<Integer> supplier;
    private final Consumer<Integer> consumer;

    public ZoomHandler(Consumer<Integer> consumer,
                       Supplier<Integer> supplier) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    public void set(int scale) {
        consumer.accept(scale);
    }

    public int get() {
        return supplier.get();
    }
}
