package tech.skagedal.javaaoc.tools;

import java.util.stream.IntStream;

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
}
