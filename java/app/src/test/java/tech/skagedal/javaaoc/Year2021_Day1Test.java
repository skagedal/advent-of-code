package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Year2021_Day1Test {
    @Test
    void part1() {
        final var answer = new Year2021_Day1().part1();

        assertEquals(1167, answer);
    }

    @Test
    void part2() {
        final var answer = new Year2021_Day1().part2();

        assertEquals(-1, answer);
    }
}