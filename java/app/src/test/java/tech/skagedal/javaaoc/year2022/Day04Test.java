package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day04Test {
    private final Day04 day = new Day04();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(450, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(837, answer);
    }

}