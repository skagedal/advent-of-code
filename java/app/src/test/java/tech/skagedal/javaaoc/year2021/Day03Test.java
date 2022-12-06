package tech.skagedal.javaaoc.year2021;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Day03Test {
    @Test
    void part1() {
        final var answer = new Day03().part1();

        assertEquals(3882564, answer);
    }

    @Test
    void part2() {
        final var answer = new Day03().part2();

        assertEquals(3385170, answer);
    }
}