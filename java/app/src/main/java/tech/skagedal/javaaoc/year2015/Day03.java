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
        var visitedPoints = new HashSet<Point>();
        var point = Point.ZERO;

        visitedPoints.add(point);

        final var vectorStream = getDirections(context);
        for (var vector : iterate(vectorStream)) {
            point = point.plus(vector);
            visitedPoints.add(point);
        }

        return visitedPoints.size();
    }

    public long part2(AdventContext context) {
        var visitedPoints = new HashSet<Point>();
        var point = Point.ZERO;
        var roboPoint = Point.ZERO;

        visitedPoints.add(point);

        final var vectorStream = getDirections(context);
        for (var enumeratedVector : iterate(Streams.enumerated(vectorStream))) {
            if (enumeratedVector.number() % 2 == 0) {
                point = point.plus(enumeratedVector.value());
                visitedPoints.add(point);
            } else {
                roboPoint = roboPoint.plus(enumeratedVector.value());
                visitedPoints.add(roboPoint);
            }
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
