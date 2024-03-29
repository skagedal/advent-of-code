package tech.skagedal.javaaoc.tools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import tech.skagedal.javaaoc.tools.streamsetc.SpliteratorUtil;
import tech.skagedal.javaaoc.tools.string.StringSpliterator;

class StringSpliteratorTest {
    @Test
    void collect_to_list() {
        final var spliterator = new StringSpliterator(new String[]{"One", "Two", "Three"});
        final var stream = StreamSupport.stream(spliterator, false);
        final var list = stream.toList();

        assertEquals(List.of("One", "Two", "Three"), list);
    }

    @Test
    void check_characteristics() {
        final var simpleStream = Stream.of("One", "Two", "Three");
        assertEquals(
            "ORDERED, SIZED, IMMUTABLE, SUBSIZED",
            SpliteratorUtil.describeSpliterator(simpleStream.spliterator())
        );

        final var infiniteStream = Stream.iterate(0, i -> i + 1);
        assertEquals(
            "ORDERED, IMMUTABLE",
            SpliteratorUtil.describeSpliterator(infiniteStream.spliterator())
        );
    }
}