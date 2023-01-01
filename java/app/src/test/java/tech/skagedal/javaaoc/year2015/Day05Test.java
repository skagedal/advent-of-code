package tech.skagedal.javaaoc.year2015;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day05Test {
    @Test
    void naughty_or_nice() {
        assertTrue(Day05.isNice("ugknbfddgicrmopn"));
        assertTrue(Day05.isNice("aaa"));
        assertFalse(Day05.isNice("jchzalrnumimnmhp"));
        assertFalse(Day05.isNice("haegwjzuvuyypxyu"));
        assertFalse(Day05.isNice("dvszwmarrgswjxmb"));
    }
}