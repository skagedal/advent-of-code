package tech.skagedal.javaaoc.year2015;

import static tech.skagedal.javaaoc.tools.streamsetc.Streams.iterate;

import java.util.HashSet;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.geom.Point;
import tech.skagedal.javaaoc.tools.geom.Vector;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Perfectly Spherical Houses in a Vacuum"
)
public class Day03 {
    public long part1(AdventContext context) {
        return findVisitedPoints(getDirections(context), 1);
    }

    public long part2(AdventContext context) {
        return findVisitedPoints(getDirections(context), 2);
    }

    private int findVisitedPoints(Stream<Vector> directions, int numberOfWorkers) {
        var visitedPoints = new HashSet<Point>();
        var currentPoints = Stream.generate(() -> Point.ZERO).limit(numberOfWorkers).toArray(Point[]::new);

        visitedPoints.add(Point.ZERO);

        for (var enumeratedVector : iterate(Streams.enumerated(directions))) {
            final var worker = enumeratedVector.number() % currentPoints.length;
            currentPoints[worker] = currentPoints[worker].plus(enumeratedVector.value());
            visitedPoints.add(currentPoints[worker]);
        }

        return visitedPoints.size();
    }

    private Stream<Vector> getDirections(AdventContext context) {
        return context.chars().mapToObj(this::vectorForChar);
    }

    private Vector vectorForChar(int c) {
        return switch (c) {
            case '>' -> Vector.RIGHT;
            case '<' -> Vector.LEFT;
            case '^' -> Vector.UP;
            case 'v' -> Vector.DOWN;
            default -> throw new IllegalArgumentException("Unknown direction char: " + c);
        };
    }
}
