package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Year2021_Day03Test {
    @Test
    void part1() {
        final var answer = new Year2021_Day03().part1();

        assertEquals(3882564, answer);
    }

    @Disabled
    @Test
    void part2() {
        final var answer = new Year2021_Day03().part2();

        assertEquals(-1, answer);
    }
}