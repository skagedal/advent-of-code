package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day03Test {
    private final Day03 day = new Day03();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(7691, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(2508, answer);
    }

}