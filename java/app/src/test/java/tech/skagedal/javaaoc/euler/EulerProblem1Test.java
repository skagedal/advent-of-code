package tech.skagedal.javaaoc.euler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EulerProblem1Test {
    @Test
    void part1() {
        Assertions.assertEquals(
            4613732,
            new EulerProblem1().part1()
        );
    }
}