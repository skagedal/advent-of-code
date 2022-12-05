package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Range;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Day04 extends AocDay {
    public long part1() {
        return readLines()
            .map(Day04::parseTuplesOfRanges)
            .filter(pair -> pair.value1().encloses(pair.value2()) || pair.value2().encloses(pair.value1()))
            .count();
    }

    public long part2() {
        return readLines()
            .map(Day04::parseTuplesOfRanges)
            .filter(pair -> pair.value1().isConnected(pair.value2()))
            .count();
    }

    private static Tuple2<Range<Integer>, Range<Integer>> parseTuplesOfRanges(String s) {
        final var split = s.split(",");
        return new Tuple2<>(parseRange(split[0]), parseRange(split[1]));
    }

    private static Range<Integer> parseRange(String s) {
        final var split = s.split("-");
        return Range.closed(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }
}
