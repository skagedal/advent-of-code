package tech.skagedal.javaaoc.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Iterators {
    public static <T, U> Iterator<List<U>> splitting(Iterator<T> iterator, Predicate<T> isPivot, Function<T, U> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<U> next() {
                final var list = new ArrayList<U>();
                while (iterator.hasNext()) {
                    final var element = iterator.next();
                    if (isPivot.test(element)) {
                        return list;
                    } else {
                        list.add(mapper.apply(element));
                    }
                }
                return list;
            }
        };
    }

    public static <T> Iterator<List<T>> splittingFixedSize(Iterator<T> iterator, long size) {
        return new IteratorSplittingFixedSizeOverlapping<>(iterator, size, 0);
    }

    public static <T> Iterator<List<T>> splittingFixedSizeOverlapping(Iterator<T> iterator, long size, long overlap) {
        return new IteratorSplittingFixedSizeOverlapping<>(iterator, size, overlap);
    }

    public static <T> Iterator<T> zip(PrimitiveIterator.OfInt iterator1, PrimitiveIterator.OfInt iterator2, BiIntFunction<T> combiner) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public T next() {
                return combiner.apply(iterator1.next(), iterator2.next());
            }
        };
    }
}
