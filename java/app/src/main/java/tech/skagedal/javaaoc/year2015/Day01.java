package tech.skagedal.javaaoc.year2015;

import java.util.stream.IntStream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Not Quite Lisp"
)
public class Day01 {
    public long part1(AdventContext context) {
        return getIntStream(context).sum();
    }

    public long part2(AdventContext context) {
        var i = 0;
        for (var j : Streams.iterate(Streams.enumerated(getIntStream(context).boxed()))) {
            i += j.value();
            if (i == -1) {
                return j.number() + 1;
            }
        }
        throw new IllegalStateException("No answer");
    }

    private static IntStream getIntStream(AdventContext context) {
        return context.chars()
            .map(i -> switch (i) {
                case '(' -> 1;
                case ')' -> -1;
                default -> 0;
            });
    }
}
