package tech.skagedal.javaaoc.tools.streamsetc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class YieldChannelIteratorTest {
    @Test
    void testEnds() {
        var list = new ArrayList<String>();
        for (var duck : nephewIterable()) {
            list.add(duck);
        }
        assertEquals(
            List.of("Huey", "Dewey", "Louie"),
            list
        );
    }

    @Test
    void testThrows() {
        var list = new ArrayList<String>();
        assertThrows(DuckException.class, () -> {
            for (var duck : cousinIterable()) {
                list.add(duck);
            }
        });
        assertEquals(List.of("Gladstone"), list);
    }

    static Iterable<String> nephewIterable() {
        return () -> new YieldChannelIterator<>(channel -> {
            channel.yield("Huey");
            channel.yield("Dewey");
            channel.yield("Louie");
        });
    }

    static Iterable<String> cousinIterable() {
        return () -> new YieldChannelIterator<>(channel -> {
            channel.yield("Gladstone");
            throw new DuckException();
        });
    }

    static class DuckException extends RuntimeException { }
}