package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Year2022_Day03Test {
    private final Year2022_Day03 day = new Year2022_Day03();

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