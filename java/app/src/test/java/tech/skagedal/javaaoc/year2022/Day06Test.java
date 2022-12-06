package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day06Test {
    private final Day06 day = new Day06();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(1538, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(2315, answer);
    }

}