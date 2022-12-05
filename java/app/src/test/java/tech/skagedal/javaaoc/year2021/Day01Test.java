package tech.skagedal.javaaoc.year2021;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day01Test {
    @Test
    void part1() {
        final var answer = new Day01().part1();

        assertEquals(1167, answer);
    }

    @Test
    void part2() {
        final var answer = new Day01().part2();

        assertEquals(1130, answer);
    }
}