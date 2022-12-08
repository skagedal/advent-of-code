package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day08Test {
    private final Day08 day = new Day08();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(1698, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(672280, answer);
    }

}