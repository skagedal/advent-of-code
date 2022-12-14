package tech.skagedal.javaaoc.year2021;

import static tech.skagedal.javaaoc.tools.streamsetc.Streams.iterate;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.streamsetc.IntStreams;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.linear.Point;
import tech.skagedal.javaaoc.tools.regex.Patterns;

@AdventOfCode
public class Day05 {
    public long part1(AdventContext context) {
        return findOverlappingPoints(context.lines().map(Line::parse).filter(Line::isStraight));
    }

    public long part2(AdventContext context) {
        return findOverlappingPoints(context.lines().map(Line::parse));
    }

    private static long findOverlappingPoints(Stream<Line> lines) {
        final var map = new HashMap<Point, Integer>();
        for (var p : iterate(lines.flatMap(Line::points))) {
            map.put(p, map.getOrDefault(p, 0) + 1);
        }
        return map.values().stream().filter(i -> i > 1).count();
    }

    // TODO: Use Point's own parsing
    private static final Pattern PATTERN = Pattern.compile("^(\\d+),(\\d+) -> (\\d+),(\\d+)$");

    private record Line(Point a, Point b) {
        static Line parse(String line) {
            final var match = Patterns.match(PATTERN, line);
            return new Line(
                new Point(Integer.parseInt(match.group(1)), Integer.parseInt(match.group(2))),
                new Point(Integer.parseInt(match.group(3)), Integer.parseInt(match.group(4)))
            );
        }

        boolean isStraight() {
            return a.x() == b.x() || a.y() == b.y();
        }

        Stream<Point> points() {
            return a.interpolateTo(b);
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day05());
    }
}
