package tech.skagedal.javaaoc.year2015;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tech.skagedal.javaaoc.aoc.AdventContext;

class Day01Test {
    Day01 day = new Day01();

    @Test
    void examples() {
        assertEquals(
            day.part1(AdventContext.fromString("(())")),
            0
        );
        assertEquals(
            day.part1(AdventContext.fromString("()()")),
            0
        );
        assertEquals(
            day.part1(AdventContext.fromString("))(((((")),
            3
        );

    }
}