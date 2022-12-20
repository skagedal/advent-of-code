package tech.skagedal.javaaoc.year2022;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;

@AdventOfCode(
    description = "Grove Positioning System"
)
public class Day20 {
    public long part1(AdventContext context) {
        final var arr = context.lines().mapToInt(Integer::parseInt).toArray();
        return 0;
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day20(), """
            1
            2
            -3
            3
            -2
            0
            4""");
    }
}
