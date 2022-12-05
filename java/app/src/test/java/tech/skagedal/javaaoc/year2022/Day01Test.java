package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day01Test {
    private final Day01 day = new Day01();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(70509, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(208567, answer);
    }
}