package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Year2022_Day01Test {
    private final Year2022_Day01 day = new Year2022_Day01();

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