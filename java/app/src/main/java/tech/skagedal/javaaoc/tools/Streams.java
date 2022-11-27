package tech.skagedal.javaaoc.tools;

import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streams {
    public static <T1, T2> Stream<Tuple2<T1, T2>> zip(Stream<T1> s1, Stream<T2> s2) {
        return zip(s1, s2, Tuple2::new);
    }

    public static <T1, T2, U> Stream<U> zip(Stream<T1> s1, Stream<T2> s2, BiFunction<T1, T2, U> combiner) {
        return sequentialStream(new Zip2Spliterator<>(s1.spliterator(), s2.spliterator(), combiner));
    }

    private static <T1, T2, U> Stream<U> sequentialStream(Zip2Spliterator<T1, T2, U> spliterator) {
        return StreamSupport.stream(
            spliterator,
            false
        );
    }
}
