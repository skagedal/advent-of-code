package tech.skagedal.javaaoc.year2021;

import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Day01 extends AocDay {
    public long part1() {
        return Streams.splittingToTuple2Overlapping(getLongs())
            .filter(Day01::isIncreasing)
            .count();
    }

    public long part2() {
        return Streams.splittingToTuple2Overlapping(smoothen(getLongs()))
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

    private Stream<Long> getLongs() {
        return readLines().map(Long::valueOf);
    }
}
