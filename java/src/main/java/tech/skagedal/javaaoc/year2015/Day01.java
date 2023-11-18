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
        return Streams.enumerated(Streams.accumulate(getIntStream(context).boxed(), 0, Integer::sum))
            .filter(enumerated -> enumerated.value() == -1)
            .findFirst().orElseThrow()
            .number() + 1;
    }

    private static IntStream getIntStream(AdventContext context) {
        return context.chars()
            .map(character -> switch (character) {
                case '(' -> 1;
                case ')' -> -1;
                default -> throw new IllegalArgumentException("Expected only ( and ) in stream");
            });
    }
}
