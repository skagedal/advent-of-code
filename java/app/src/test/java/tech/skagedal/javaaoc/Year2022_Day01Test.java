package tech.skagedal.javaaoc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Year2022_Day01Test {
    @Test
    void part1() {
        final var answer = new Year2022_Day01().part1();

        assertEquals(70509, answer);;
    }
}