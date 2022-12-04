package tech.skagedal.javaaoc.tools;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteratorSplittingFixedSizeOverlapping<T> implements Iterator<List<T>> {
    private final Iterator<T> iterator;
    private final long size;
    private final long overlap;
    private final Deque<T> deque = new LinkedList<>();

    public IteratorSplittingFixedSizeOverlapping(Iterator<T> iterator, long size, long overlap) {
        this.iterator = iterator;
        this.size = size;
        this.overlap = overlap;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public List<T> next() {
        // Remove old elements
        for (var i = overlap; i < size; i++) {
            deque.pollFirst();
        }

        // Fill up
        for (var i = deque.size(); i < size && iterator.hasNext(); i++) {
            deque.addLast(iterator.next());
        }

        return new ArrayList<>(deque);
    }
}
