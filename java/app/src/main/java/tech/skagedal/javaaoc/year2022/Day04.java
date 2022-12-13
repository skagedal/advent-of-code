package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Range;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;

@AdventOfCode
public class Day04 {
    public long part1(AdventContext context) {
        return context.lines()
            .map(RangePair::fromString)
            .filter(RangePair::oneEnclosesTheOther)
            .count();
    }

    public long part2(AdventContext context) {
        return context.lines()
            .map(RangePair::fromString)
            .filter(RangePair::connected)
            .count();
    }

    private record RangePair(Range<Integer> range1, Range<Integer> range2) {
        static RangePair fromString(String s) {
            final var split = s.split(",");
            return new RangePair(parseRange(split[0]), parseRange(split[1]));
        }

        boolean oneEnclosesTheOther() {
            return range1.encloses(range2) || range2.encloses(range1);
        }

        boolean connected() {
            return range1.isConnected(range2);
        }

    }

    private static Range<Integer> parseRange(String s) {
        final var split = s.split("-");
        return Range.closed(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }
}
