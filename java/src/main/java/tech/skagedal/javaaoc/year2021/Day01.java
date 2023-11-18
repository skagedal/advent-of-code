package tech.skagedal.javaaoc.year2021;

import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.function.Tuple2;

@AdventOfCode
public class Day01 {
    public long part1(AdventContext context) {
        return Streams.splittingToTuple2Overlapping(getLongs(context.lines()))
            .filter(Day01::isIncreasing)
            .count();
    }

    public long part2(AdventContext context) {
        return Streams.splittingToTuple2Overlapping(smoothen(getLongs(context.lines())))
            .filter(Day01::isIncreasing)
            .count();
    }

    private Stream<Long> smoothen(Stream<Long> stream) {
        return Streams.splittingFixedSizeOverlapping(
                stream, 3, 2)
            .map(list -> list.stream().mapToLong(Long::longValue).sum());
    }

    static private boolean isIncreasing(Tuple2<Long, Long> tuple) {
        return tuple.value2() > tuple.value1();
    }

    private Stream<Long> getLongs(Stream<String> lines) {
        return lines.map(Long::valueOf);
    }
}
