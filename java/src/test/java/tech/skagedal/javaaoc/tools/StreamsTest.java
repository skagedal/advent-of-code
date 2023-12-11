package tech.skagedal.javaaoc.tools;

import org.junit.jupiter.api.Test;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamsTest {
    @Test
    void zip2() {
        final var zipped = Streams
            .zip(Stream.of("One", "Two"), Stream.of(List.of("A")))
            .toList();

        assertEquals(
            List.of(new Tuple2<>("One", List.of("A"))),
            zipped
        );
    }

    @Test
    void allPairs() {
        assertEquals(
            Set.of(new Tuple2<>("one", "two"), new Tuple2<>("one", "three"), new Tuple2<>("two", "three")),
            Streams.allPairs(List.of("one", "two", "three")).collect(Collectors.toSet())
        );
    }
}
