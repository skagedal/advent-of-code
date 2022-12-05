package tech.skagedal.javaaoc.year2021;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day02Test {
    @Test
    void part1() {
        final var answer = new Day02().part1();

        assertEquals(2102357, answer);
    }

    @Test
    void part2() {
        final var answer = new Day02().part2();

        assertEquals(2101031224, answer);
    }
}