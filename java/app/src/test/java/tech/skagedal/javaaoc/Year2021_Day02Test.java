package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Year2021_Day02Test {
    @Test
    void part1() {
        final var answer = new Year2021_Day02().part1();

        assertEquals(2102357, answer);
    }

    @Test
    void part2() {
        final var answer = new Year2021_Day02().part2();

        assertEquals(2101031224, answer);
    }
}