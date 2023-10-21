package tech.skagedal.javaaoc.year2015;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day05Test {
    @Test
    void naughty_or_nice() {
        assertTrue(Day05.isNiceInPart1("ugknbfddgicrmopn"));
        assertTrue(Day05.isNiceInPart1("aaa"));
        assertFalse(Day05.isNiceInPart1("jchzalrnumimnmhp"));
        assertFalse(Day05.isNiceInPart1("haegwjzuvuyypxyu"));
        assertFalse(Day05.isNiceInPart1("dvszwmarrgswjxmb"));
    }
}