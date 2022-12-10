package tech.skagedal.javaaoc.year2021;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;
import tech.skagedal.javaaoc.tools.linear.Point;
import tech.skagedal.javaaoc.tools.regex.Patterns;

// @AdventOfCode
public class Day05 extends AocDay {
    public long part1() {
        final var map = new HashMap<Point, Integer>();
        for (var pair : Streams.toIterable(readLines().map(Day05::parse))) {
            for (var p : Streams.toIterable(straightLine(pair.value1(), pair.value2()))) {

            }
        }
        return 0;
    }

    private static final Pattern PATTERN = Pattern.compile("^(\\d+),(\\d+) -> (\\d+),(\\d+)$");
    private static Tuple2<Point, Point> parse(String line) {
        final var match = Patterns.match(PATTERN, line);
        return new Tuple2<>(
            new Point(Integer.parseInt(match.group(1)), Integer.parseInt(match.group(2))),
            new Point(Integer.parseInt(match.group(3)), Integer.parseInt(match.group(4)))
        );
    }

    private static Stream<Point> straightLine(Point p1, Point p2) {
        if (p1.x() == p2.x()) {
            // TODO
            return Stream.of();
        } else if (p1.y() == p2.y()) {
            // TODO
            return Stream.of();
        } else {
            return Stream.of();
        }
    }
}
