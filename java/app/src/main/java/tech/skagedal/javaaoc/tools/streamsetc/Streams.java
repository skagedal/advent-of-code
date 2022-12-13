package tech.skagedal.javaaoc.tools.streamsetc;

import com.google.common.base.Functions;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.function.Tuple3;
import tech.skagedal.javaaoc.tools.function.Function3;

public class Streams {
    public static <T> Stream<List<T>> splitting(Stream<T> stream, Predicate<T> isPivot) {
        return Streams.fromIterator(Iterators.splitting(stream.iterator(), isPivot, Functions.identity()));
    }

    public static <T, U> Stream<List<U>> splitting(Stream<T> stream, Predicate<T> isPivot, Function<T, U> mapper) {
        return Streams.fromIterator(Iterators.splitting(stream.iterator(), isPivot, mapper));
    }

    public static <T> Stream<List<T>> splittingFixedSize(Stream<T> stream, long size) {
        return Streams.fromIterator(Iterators.splittingFixedSize(stream.iterator(), size));
    }

    public static <T> Stream<List<T>> splittingFixedSizeOverlapping(Stream<T> stream, long size, long overlap) {
        return Streams.fromIterator(Iterators.splittingFixedSizeOverlapping(stream.iterator(), size, overlap));
    }

    public static <T> Stream<Tuple2<T, T>> splittingToTuple2Overlapping(Stream<T> stream) {
        return splittingFixedSizeOverlapping(stream, 2, 1)
            .map(list -> new Tuple2<>(list.get(0), list.get(1)));
    }

    public static <T> Stream<T> fromIterator(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    public static <T> Iterable<T> iterate(Stream<T> stream) {
        return () -> stream.iterator();
    }

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

    public static <T> Stream<Enumerated<T>> enumerated(Stream<T> s) {
        return zip(IntStream.iterate(0, i -> i + 1).boxed(), s, Enumerated::new);
    }

    public static <T extends Comparable<T>> boolean isSorted(Stream<T> stream) {
        return Streams.splittingToTuple2Overlapping(stream).allMatch(tuple ->
            tuple.value1().compareTo(tuple.value2()) <= 0);
    }


    private static <T> Stream<T> sequentialStream(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }
}
