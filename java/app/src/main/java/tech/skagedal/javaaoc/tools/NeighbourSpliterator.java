package tech.skagedal.javaaoc.tools;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class NeighbourSpliterator<T> implements Spliterator<List<T>> {
    private final Spliterator<T> spliterator;
    private final Iterator<T> iterator;
    private final long neighbourhoodLength;
    private final long estimatedSize;
    private final long seen = 0;
    private final Deque<T> deque = new LinkedList<>();

    public NeighbourSpliterator(Spliterator<T> spliterator, long neighbourhoodLength) {
        this.neighbourhoodLength = neighbourhoodLength;
        this.spliterator = spliterator;
        this.iterator = Spliterators.iterator(spliterator);
        this.estimatedSize = spliterator.estimateSize();
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> action) {
        while (seen < neighbourhoodLength) {

        }
        if (iterator.hasNext()) {

        }
        return false;
    }

    @Override
    public Spliterator<List<T>> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
