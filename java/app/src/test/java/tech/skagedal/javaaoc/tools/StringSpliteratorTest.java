package tech.skagedal.javaaoc.tools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

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
        System.out.println(SpliteratorUtil.describeSpliterator(simpleStream.spliterator()));

        final var infiniteStream = Stream.iterate(0, i -> i + 1);
        System.out.println(SpliteratorUtil.describeSpliterator(infiniteStream.spliterator()));
    }
}