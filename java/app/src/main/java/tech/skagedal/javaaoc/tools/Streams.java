package tech.skagedal.javaaoc.tools;

import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streams {
    public static <T1, T2> Stream<Tuple2<T1, T2>> zip(Stream<T1> s1, Stream<T2> s2) {
        return zip(s1, s2, Tuple2::new);
    }

    public static <T1, T2, U> Stream<U> zip(Stream<T1> s1, Stream<T2> s2, BiFunction<T1, T2, U> combiner) {
        return sequentialStream(new Zip2Spliterator<>(s1.spliterator(), s2.spliterator(), combiner))
            .onClose(s1::close)
            .onClose(s2::close);
    }

    public static <T1, T2, T3> Stream<Tuple3<T1, T2, T3>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3) {
        return zip(s1, s2, s3, Tuple3::new);
    }

    public static <T1, T2, T3, R> Stream<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Function3<T1, T2, T3, R> combiner) {
        return sequentialStream(new Zip3Spliterator<>(s1.spliterator(), s2.spliterator(), s3.spliterator(), combiner))
            .onClose(s1::close)
            .onClose(s2::close)
            .onClose(s3::close);
    }

    private static <T> Stream<T> sequentialStream(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }
}