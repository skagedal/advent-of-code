package tech.skagedal.javaaoc.year2022;

import com.google.common.primitives.Longs;
import java.util.OptionalLong;
import java.util.Stack;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.format.Term;
import tech.skagedal.javaaoc.tools.linear.Grid;
import tech.skagedal.javaaoc.tools.linear.Point;

public class Day12 extends AocDay {
    public long part1() {
        return solvePart1(readExampleLines());
    }

    public long solvePart1(Stream<String> lines) {
        return solvePart1(lines, Square.LOWEST_HEIGHT);
    }

    public long solvePart1(Stream<String> lines, int startHeight) {
        var grid = Grid.fromLines(lines, Square.fromIntWithStartHeight(startHeight));
        var start = grid
            .allPoints()
            .filter(point -> grid.get(point).distance instanceof Distance.Start)
            .findFirst().orElseThrow();

        var stack = new Stack<Point>();
        long returnval = explore(start, grid, "", stack).orElseThrow();
        return returnval;

    }

    private OptionalLong explore(Point point, Grid<Square> grid, String indent, Stack<Point> stack) {
        // This isn't working.

        stack.push(point);
        System.out.printf(indent + "Exploring point %s...\n", point);
        final var square = grid.get(point);

        grid.printGrid(p -> {
            final var s = grid.get(p);
            final var startAnsi = p.equals(point) ? Term.FG_GREEN : (stack.contains(p) ? Term.FG_YELLOW : "");
            final var stopAnsi = Term.RESET;
            return startAnsi + switch (s.distance) {
                case Distance.Known known -> String.format("%02d", known.steps());
                case Distance.Unknown unknown -> "--";
                case Distance.Unreachable unreachable -> "!!";
                case Distance.Start start -> "SS";
            } + stopAnsi + " ";
        });

        long[] longs = Grid.fourDirections()
            .map(point::plus)
            .filter(grid::isInBounds)
            .filter(Predicate.not(stack::contains))
            .flatMapToLong(point2 -> consider(grid, square, point, point2, indent + " ", stack).stream())
            .toArray();

        if (longs.length > 0) {
            Distance.Known knownDistance = new Distance.Known(Longs.min(longs) + 1);
            square.distance = knownDistance;
            stack.pop();
            return OptionalLong.of(knownDistance.steps());
        } else {
            square.distance = Distance.UNREACHABLE;
            stack.pop();
            return OptionalLong.empty();
        }
    }

    private OptionalLong consider(Grid<Square> grid, Square square, Point point, Point nextPoint, String indent,
                                  Stack<Point> stack) {
        final var nextSquare = grid.get(nextPoint);
        System.out.printf(indent + "Testing point %s -> %s... ", point, nextPoint);
        if (nextSquare.height > square.height + 1) {
            System.out.printf("too high; can't reach %c from %c\n", nextSquare.height, square.height);
            return OptionalLong.empty();
        }
        System.out.printf("yes, can reach %c from %c\n", nextSquare.height, square.height);
        return switch (nextSquare.distance) {
            case Distance.Known known -> OptionalLong.of(known.steps());
            case Distance.Unknown unknown -> explore(nextPoint, grid, indent + " ", stack);
            case Distance.Unreachable unreachable -> OptionalLong.empty();
            case Distance.Start start -> OptionalLong.empty();
        };
    }

    private static class Square {
        private static final int LOWEST_HEIGHT = 'a';
        private static final int HEIGHEST_HEIGHT = 'z';
        int height;
        Distance distance;

        public Square(int height, Distance distance) {
            assert height >= LOWEST_HEIGHT && height <= HEIGHEST_HEIGHT;
            this.height = height;
            this.distance = distance;
        }

        static Square from(int integer) {
            return fromIntWithStartHeight(LOWEST_HEIGHT).apply(integer);
        }

        public static IntFunction<Square> fromIntWithStartHeight(int startHeight) {
            return integer -> switch (integer) {
                case 'S' -> new Square(startHeight, Distance.START);
                case 'E' -> new Square(HEIGHEST_HEIGHT, new Distance.Known(0));
                default -> new Square(integer, Distance.UNKNOWN);
            };
        }
    }

    private sealed interface Distance {
        Distance START = new Start();
        Distance UNKNOWN = new Unknown();
        Distance UNREACHABLE = new Unreachable();
        record Start() implements Distance {}
        record Unknown() implements Distance {}
        record Known(long steps) implements Distance {}
        record Unreachable() implements Distance {}
    }

    public static void main(String[] args) {
        long part = new Day12().part1();
        System.out.println("\n\nThe value is: " + part);
    }
}
