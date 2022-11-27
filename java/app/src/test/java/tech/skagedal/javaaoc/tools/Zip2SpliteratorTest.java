package tech.skagedal.javaaoc.tools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

class Zip2SpliteratorTest {
    @Test
    void simple_zip_first_ends_first() {
        final var s1 = List.of("One", "Two").spliterator();
        final var s2 = List.of("A", "B", "C").spliterator();
        final var zipped = new Zip2Spliterator<>(s1, s2, Tuple2::new);
        final var zippedList = StreamSupport.stream(zipped, false).toList();

        assertEquals(
            List.of(new Tuple2<>("One", "A"), new Tuple2<>("Two", "B")),
            zippedList
        );
    }

    @Test
    void simple_zip_second_ends_first() {
        final var s1 = List.of("One", "Two", "Three").spliterator();
        final var s2 = List.of("A").spliterator();
        final var zipped = new Zip2Spliterator<>(s1, s2, Tuple2::new);
        final var zippedList = StreamSupport.stream(zipped, false).toList();

        assertEquals(
            List.of(new Tuple2<>("One", "A")),
            zippedList
        );
    }
}