package tech.skagedal.javaaoc.tools.streamsetc;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class YieldChannelIterator<T> implements Iterator<T> {
    private final Consumer<YieldChannel<T>> generator;
    private final BlockingQueue<Event<T>> queue = new LinkedBlockingQueue<>(1);
    private final YieldChannel<T> yieldChannel = value -> {
        try {
            queue.put(new Event.Value<>(value));
        } catch (InterruptedException e) {
            // Not sure what to do here
            throw new RuntimeException(e);
        }
    };
    private Thread thread;
    private Event<T> next;

    private sealed interface Event<T> {
        record Value<T>(T value) implements Event<T> {}
        record Exception<T>(RuntimeException exception) implements Event<T> {}
        record End<T>() implements Event<T> {}
    }

    public YieldChannelIterator(Consumer<YieldChannel<T>> generator) {
        this.generator = generator;
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            next = fetchNext();
        }
        return !(next instanceof Event.End<T>);
    }

    @Override
    public T next() {
        if (next == null) {
            next = fetchNext();
        }
        final var returnValue = switch (next) {
            case Event.Value<T> value -> value.value;
            case Event.Exception<T> exception -> throw exception.exception;
            case Event.End<T> end -> throw new NoSuchElementException();
        };
        next = null;
        return returnValue;
    }

    private Event<T> fetchNext() {
        if (thread == null) {
            thread = Thread.startVirtualThread(this::startGenerator);
        }
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return new Event.End<>();
        }
    }

    private void startGenerator() {
        try {
            try {
                generator.accept(yieldChannel);
                queue.put(new Event.End<>());
            } catch (RuntimeException exception) {
                queue.put(new Event.Exception<>(exception));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
