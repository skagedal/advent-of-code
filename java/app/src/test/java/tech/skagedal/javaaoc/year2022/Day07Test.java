package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day07Test {
    private final Day07 day = new Day07();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(2031851, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(2568781, answer);
    }

}