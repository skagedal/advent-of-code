package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Longs;

import java.util.Arrays;
import java.util.stream.Stream;

@AdventOfCode(
    description = ""
)
public class Day09 {
    public long part1(AdventContext context) {
        return context.lines()
            .map(Longs::inString)
            .mapToLong(Day09::findNextExtrapolatedValue)
            .sum();
    }

    public long part2(AdventContext context) {
        return 0;
    }
    
    public static long findNextExtrapolatedValue(Stream<Long> valuesStream) {
        return findNextExtrapolatedValue(valuesStream.mapToLong(Long::longValue).toArray());
    }

    public static long findNextExtrapolatedValue(long[] values) {
        if (Arrays.stream(values).allMatch(l -> l == 0)) {
            return 0;
        }
        return values[values.length - 1] + findNextExtrapolatedValue(diffs(values));
    }

    public static long[] diffs(long[] values) {
        long[] out = new long[values.length - 1];
        for (int i = 0; i < values.length - 1; i++) {
            out[i] = values[i + 1] - values[i];
        }
        return out;
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day09(), """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45""");
        AdventOfCodeRunner.run(new Day09());
    }
}
