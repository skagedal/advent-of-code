package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day05Test {
    private final Day05 day = new Day05();

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