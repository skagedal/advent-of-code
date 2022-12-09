package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.IntStreams;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.linear.Point;
import tech.skagedal.javaaoc.tools.linear.Vector;

@AdventOfCode
public class Day09 extends AocDay {
    public long part1() {
        var head = new Point(0, 0);
        var tail = new Point(0, 0);
        var visitedLocations = Sets.newHashSet(tail);

        for (var move : Streams.toIterable(readLines().map(Day09::parse).flatMap(Day09::steps))) {
            head = head.plus(move);
            if (!touches(head, tail)) {
                tail = tail.plus(clamped(head.minus(tail)));
                visitedLocations.add(tail);
            }
        }
        return visitedLocations.size();
    }

    private static Stream<Vector> steps(Vector vector) {
        final var remaining = new AtomicReference<>(vector);
        return Stream.generate(() -> {
            Vector v = remaining.get();
            final var diff = clamped(v);
            remaining.set(v.minus(diff));
            return diff;
        }).takeWhile(Vector::isNonZero);
    }

    public long part2() {
        return 0;
    }

    private static final Pattern VECTOR_PATTERN = Pattern.compile("^([LURD])\\s(\\d+)$");

    private static Vector parse(String line) {
        final var matcher = VECTOR_PATTERN.matcher(line);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("could not parse " + line + " - no match");
        }
        final var match = matcher.toMatchResult();
        final var magnitude = Integer.parseInt(match.group(2));
        return switch (match.group(1).charAt(0)) {
            case 'U' -> new Vector(0, -magnitude);
            case 'D' -> new Vector(0, magnitude);
            case 'R' -> new Vector(magnitude, 0);
            case 'L' -> new Vector(-magnitude, 0);
            default -> throw new IllegalArgumentException("could not parse " + line);
        };
    }

    private static boolean touches(Point a, Point b) {
        final var diff = a.minus(b);
        return Math.abs(diff.dx()) <= 1 && Math.abs(diff.dy()) <= 1;
    }

    private static Vector clamped(Vector v) {
        return new Vector(clamped(v.dx()), clamped(v.dy()));
    }

    private static int clamped(int i) {
        return Integer.signum(i) * Math.min(1, Math.abs(i));
    }
}
