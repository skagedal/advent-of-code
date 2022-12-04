package tech.skagedal.javaaoc;

import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Year2021_Day1 extends Year2021Day {
    public long part1() {
        return Streams.splittingToTuple2Overlapping(getLongs())
            .filter(Year2021_Day1::isIncreasing)
            .count();
    }

    public long part2() {
        return Streams.splittingToTuple2Overlapping(smoothen(getLongs()))
            .filter(Year2021_Day1::isIncreasing)
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
        return getLines().map(Long::valueOf);
    }

    private Stream<String> getLines() {
        return readLines("day01_input.txt");
    }
}
