package tech.skagedal.javaaoc.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class Iterators {
    public static <T> Iterator<List<T>> splitting(Iterator<T> iterator, Predicate<T> isPivot) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<T> next() {
                final var list = new ArrayList<T>();
                while (iterator.hasNext()) {
                    final var element = iterator.next();
                    if (isPivot.test(element)) {
                        return list;
                    } else {
                        list.add(element);
                    }
                }
                return list;
            }
        };
    }
}
