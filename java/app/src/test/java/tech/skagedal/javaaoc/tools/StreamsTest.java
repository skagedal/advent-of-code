package tech.skagedal.javaaoc.tools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

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
}