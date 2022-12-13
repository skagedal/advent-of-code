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
        Stream<Line> lines = context.lines().map(Line::parse).filter(Line::isStraight);
        return findOverlappingPoints(lines);
    }

    private static long findOverlappingPoints(Stream<Line> lines) {
        final var map = new HashMap<Point, Integer>();
        for (var line : iterate(lines)) {
            for (var p : iterate(line.points())) {
                map.put(p, map.getOrDefault(p, 0) + 1);
            }
        }
        return map.values().stream().filter(i -> i > 1).count();
    }

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

        // Note that this only handles straight lines or lines at 45 degrees
        Stream<Point> points() {
            final var length = Math.max(Math.abs(a.x() - b.x()), Math.abs(a.y() - b.y()));
            final var diff = b.minus(a).dividedBy(length);
            return IntStream.rangeClosed(0, length).mapToObj(i -> a.plus(diff.times(i)));
        }
    }

    private static Stream<Point> straightLine(Point p1, Point p2) {
        if (p1.x() == p2.x()) {
            return IntStreams.rangeClosed(p1.y(), p2.y(), p2.y() < p1.y() ? -1 : 1).mapToObj(y -> new Point(p1.x(), y));
        } else if (p1.y() == p2.y()) {
            return IntStreams.rangeClosed(p1.x(), p2.x(), p2.x() < p1.x() ? -1 : 1).mapToObj(x -> new Point(x, p1.y()));
        } else {
            return Stream.of();
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day05());
    }
}
