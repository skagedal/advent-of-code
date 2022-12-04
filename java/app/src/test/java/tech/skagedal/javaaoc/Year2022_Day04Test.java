package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Year2022_Day04Test {
    private final Year2022_Day04 day = new Year2022_Day04();

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