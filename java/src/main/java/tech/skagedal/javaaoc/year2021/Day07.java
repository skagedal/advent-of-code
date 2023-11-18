package tech.skagedal.javaaoc.year2021;

import java.util.List;
import java.util.stream.LongStream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Longs;

@AdventOfCode(
    description = "The Treachery of Whales"
)
public class Day07 {
    public long part1(AdventContext context) {
        final var crabPositions = Longs.inString(context.line()).toList();

        // Bit of micro-optimization here, since it doesn't take long to just test all numbers in the range (which I
        // do in part 2).
        final var sortedArray = crabPositions.stream().mapToLong(Long::longValue).sorted().toArray();
        final int length = sortedArray.length;
        final var candidates = (length % 2 == 0)
            ? List.of(sortedArray[length / 2], sortedArray[length / 2 + 1])
            : List.of(sortedArray[length / 2]);

        return candidates.stream()
            .mapToLong(c -> fuelForPart1AlignmentPosition(crabPositions, c))
            .min().orElseThrow();
    }

    public long part2(AdventContext context) {
        final var crabPositions = Longs.inString(context.line()).toList();
        final var summary = crabPositions.stream().mapToLong(Long::longValue).summaryStatistics();
        return LongStream.rangeClosed(summary.getMin(), summary.getMax())
            .map(c -> fuelForPart2AlignmentPosition(crabPositions, c))
            .min().orElseThrow();
    }

    private long fuelForPart1AlignmentPosition(List<Long> crabPositions, long candidatePosition) {
        return crabPositions.stream()
            .mapToLong(p -> Math.abs(p - candidatePosition))
            .sum();
    }

    private long fuelForPart2AlignmentPosition(List<Long> crabPositions, long candidatePosition) {
        return crabPositions.stream()
            .mapToLong(p -> sumOfArithmeticSeries(Math.abs(p - candidatePosition)))
            .sum();
    }

    // Fun fact: I asked ChatGPT on how to simplify 1 + 2 + 3 + ... + n, and it told me to use the "sum of arithmetic
    // series" and gave me the formula.
    public static long sumOfArithmeticSeries(long n) {
        return (n + n * n) / 2;
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.run(new Day07());
    }

    private static void runExample() {
        AdventOfCodeRunner.example(new Day07(), """
            16,1,2,0,4,2,7,1,2,14""");
    }
}
