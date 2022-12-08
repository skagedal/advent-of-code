package tech.skagedal.javaaoc.tools;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntStreams {
    public static IntStream rangeClosed(int startInclusive, int endInclusive, int stride) {
        if (stride == 1) {
            return IntStream.rangeClosed(startInclusive, endInclusive);
        }
        return IntStream.iterate(
            startInclusive,
            (stride > 0) ? i -> i <= endInclusive : i -> i >= endInclusive,
            i -> i + stride
        );
    }

    public static IntStream always(int constant) {
        return IntStream.iterate(constant, i -> i);
    }

    public static <T> Stream<T> zip(IntStream stream1, IntStream stream2, BiIntFunction<T> combiner) {
        return Streams.fromIterator(Iterators.zip(stream1.iterator(), stream2.iterator(), combiner));
    }
}
