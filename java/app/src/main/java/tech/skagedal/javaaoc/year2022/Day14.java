package tech.skagedal.javaaoc.year2022;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.linear.Grid;
import tech.skagedal.javaaoc.tools.linear.Point;
import tech.skagedal.javaaoc.tools.linear.Vector;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode
public class Day14 {
    public static final Point SAND_ORIGIN = new Point(500, 0);
    public static final List<Vector> POSSIBLE_PATHS = List.of(new Vector(0, 1), new Vector(-1, 1), new Vector(1, 1));

    public long part1(AdventContext context) {
        return solve(context, context.lines().map(RockPath::fromString).toList());
    }

    public long part2(AdventContext context) {
        List<RockPath> rockPaths = context.lines().map(RockPath::fromString).toList();
        final var bottomY = rockPaths.stream().flatMap(rp -> rp.points().stream())
            .max(Comparator.comparing(Point::y)).orElseThrow().y() + 2;
        final var extraPath = new RockPath(List.of(
            new Point(SAND_ORIGIN.x() - bottomY, bottomY),
            new Point(SAND_ORIGIN.x() + bottomY, bottomY)
        ));
        return solve(context, Stream.concat(rockPaths.stream(), Stream.of(extraPath)).toList());
    }

    private int solve(AdventContext context, List<RockPath> rockPaths) {
        final var grid = Grid.enclosing(
            Stream.concat(
                rockPaths.stream().flatMap(rp -> rp.points().stream()),
                Stream.of(SAND_ORIGIN)
            ),
            point -> new Box<>(Contents.EMPTY));

        rockPaths.stream().flatMap(RockPath::interpolate).forEach(point -> grid.get(point).setValue(Contents.ROCK));

        if (context.explain()) {
            printGrid(grid);
        }
        var sandUnits = 0;
        while (tryProduceSand(grid)) {
            sandUnits++;
            if (context.explain()) {
                System.out.printf("\n== After %d units of sand: ==\n", sandUnits);
                printGrid(grid);
            }
        }
        return sandUnits;
    }

    private boolean tryProduceSand(Grid<Box<Contents>> grid) {
        var point = SAND_ORIGIN.plus(new Vector(0, -1));
        while(true) {
            final var nextPoint = next(grid, point);
            if (!grid.isInBounds(nextPoint)) {
                return false;
            }
            if (point.equals(nextPoint)) {
                grid.get(point).setValue(Contents.SAND);
                return true;
            } else {
                point = nextPoint;
            }
        }
    }

    private Point next(Grid<Box<Contents>> grid, Point point) {
        for (var delta : POSSIBLE_PATHS) {
            final var nextPoint = point.plus(delta);
            if (!grid.isInBounds(nextPoint)) {
                // Falling off into the void
                return nextPoint;
            }
            final var contents = grid.get(nextPoint).getValue();
            if (contents == Contents.EMPTY) {
                return nextPoint;
            }
        }
        return point;
    }

    private static void printGrid(Grid<Box<Contents>> grid) {
        grid.printGrid(p -> switch (grid.get(p).getValue()) {
            case EMPTY -> p.equals(SAND_ORIGIN) ? "+" : ".";
            case ROCK -> "#";
            case SAND -> "o";
        });
    }

    record RockPath(List<Point> points) {
        static RockPath fromString(String string) {
            return new RockPath(Point.parsePointsInString(string).toList());
        }

        Stream<Point> interpolate() {
            return Streams.splittingToTuple2Overlapping(points.stream())
                .flatMap(tuple -> tuple.value1().interpolateTo(tuple.value2()));
        }
    }

    enum Contents {
        EMPTY, ROCK, SAND;

        @Override
        public String toString() {
            return switch (this) {
                case EMPTY -> ".";
                case ROCK -> "#";
                case SAND -> "o";
            };
        }
    }

    static class Box<T> {
        private T value;

        Box(T value) {
            this.value = value;
        }

        T getValue() {
            return value;
        }

        void setValue(T value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        final var code = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
            """;
        System.out.println(new Day14().part2(AdventContext.fromString(code)));
    }
}
