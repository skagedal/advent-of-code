package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.skagedal.javaaoc.year2022.Day13.intx;
import static tech.skagedal.javaaoc.year2022.Day13.list;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day13Test {
    @Test
    void parse() {
        assertEquals(
            list(),
            Day13.parseLine("[]")
        );
        assertEquals(
            intx(3),
            Day13.parseLine("3")
        );
        assertEquals(
            list(intx(1), list(intx(2), list(intx(3), list(intx(4), list(intx(5), intx(6), intx(7))))), intx(8), intx(9)),
            Day13.parseLine("[1,[2,[3,[4,[5,6,7]]]],8,9]")
        );
    }
}