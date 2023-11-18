package tech.skagedal.javaaoc.year2021;

import static org.junit.jupiter.api.Assertions.*;
import static tech.skagedal.javaaoc.year2021.Day07.sumOfArithmeticSeries;

import org.junit.jupiter.api.Test;

class Day07Test {
    @Test
    void testSumOfArithmeticSeries() {
        assertEquals(
            0,
            sumOfArithmeticSeries(0)
        );
        assertEquals(
            66,
            sumOfArithmeticSeries(Math.abs(16L - 5))
        );
    }
}