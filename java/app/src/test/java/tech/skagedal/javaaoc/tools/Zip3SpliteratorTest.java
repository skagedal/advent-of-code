package tech.skagedal.javaaoc.tools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

class Zip3SpliteratorTest {
    @Test
    void zip3() {
        final var s1 = List.of("One", "Two").spliterator();
        final var s2 = List.of("A", "B", "C").spliterator();
        final var s3 = List.of(1, 2, 3, 4).spliterator();
        final var zipped = new Zip3Spliterator<>(s1, s2, s3, Tuple3::new);
        final var zippedList = StreamSupport.stream(zipped, false).toList();

        assertEquals(
            List.of(new Tuple3<>("One", "A", 1), new Tuple3<>("Two", "B", 2)),
            zippedList
        );
    }
}