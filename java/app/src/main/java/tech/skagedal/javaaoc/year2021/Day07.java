package tech.skagedal.javaaoc.year2021;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Longs;

@AdventOfCode(
    description = "The Treachery of Whales"
)
public class Day07 {
    public long part1(AdventContext context) {
        final var nums = Longs.inString(context.line()).toList();
        final var sum = nums.stream().mapToLong(Long::longValue).sum();
        final var avg = Math.round((double) sum / (double) nums.size());
        final var prod = nums.stream().mapToLong(Long::longValue).reduce(1, Longs::multiply);
        final var sqr = Math.sqrt((double) prod);
        System.out.println(sqr);

        // for every x(i)

        // sum (i=0...N){ x(i) }
        return avg;
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day07(), """
            16,1,2,0,4,2,7,1,2,14""");
    }
}
