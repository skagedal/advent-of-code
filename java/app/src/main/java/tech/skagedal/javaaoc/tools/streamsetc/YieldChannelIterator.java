package tech.skagedal.javaaoc.tools.streamsetc;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class YieldChannelIterator<T> implements Iterator<T> {
    private final Consumer<YieldChannel<T>> generator;
    private final BlockingQueue<Optional<T>> queue = new LinkedBlockingQueue<>(1);
    private final YieldChannel<T> yieldChannel = value -> {
        try {
            queue.put(Optional.of(value));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };
    private final Thread thread;
    private Optional<T> next;

    public YieldChannelIterator(Consumer<YieldChannel<T>> generator) {
        this.generator = generator;
        this.thread = Thread.startVirtualThread(() -> {
            generator.accept(yieldChannel);
            try {
                queue.put(Optional.empty());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            next = fetchNext();
        }
        return !next.isEmpty();
    }

    @Override
    public T next() {
        if (next == null) {
            next = fetchNext();
        }
        final var result = next.get();
        next = null;
        return result;
    }

    private Optional<T> fetchNext() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
