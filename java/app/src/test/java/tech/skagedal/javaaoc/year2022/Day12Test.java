package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class Day12Test {
    Day12 day12 = new Day12();

    @Test
    void testSimple() {
        assertEquals(
            25,
            day12.solvePart1(Stream.of("SbcdefghijklmnopqrstuvwxyE"))
        );
    }

    @Test
    void testSimple2() {
        assertEquals(
            27,
            day12.solvePart1(Stream.of(
                "Sbcdefghijklmnopqrstuvwxyz",
                "aaaaaaaaaaaaaaaaaaaaaaaaaz",
                "aaaaaaaaaaaaaaaaaaaaaaaaaE"
            ))
        );
    }

    @Test
    void testExampleFull() {
        var path = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi""".lines();

        assertEquals(
            31,
            day12.solvePart1(path)
        );
    }

    @Test
    void testExample2() {
        var path = """
            aabqponm
            abcrSxxl
            accszExk
            acctuvwj
            abdefghi""".lines();

        assertEquals(
            2,
            day12.solvePart1(path, 'y')
        );
    }

    @Test
    void testExample3() {
        var path = """
            aabqponm
            abcrySxl
            accszExk
            acctuvwj
            abdefghi""".lines();

        assertEquals(
            3,
            day12.solvePart1(path, 'x')
        );
    }

    @Test
    void testExample4() {
        var path = """
            aabqponm
            abcryxSl
            accszExk
            acctuvwj
            abdefghi""".lines();

        assertEquals(
            4,
            day12.solvePart1(path, 'x')
        );
    }

}