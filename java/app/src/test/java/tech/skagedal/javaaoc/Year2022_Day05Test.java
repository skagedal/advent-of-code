package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Year2022_Day05Test {
    private final Year2022_Day05 day = new Year2022_Day05();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals("VJSFHWGFT", answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals("LCTQFBVZV", answer);
    }

}