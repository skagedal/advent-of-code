package tech.skagedal.javaaoc.year2022;

import com.google.common.primitives.Longs;
import java.util.HashSet;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Stack;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.format.Term;
import tech.skagedal.javaaoc.tools.linear.Grid;
import tech.skagedal.javaaoc.tools.linear.Point;

public class Day12 extends AocDay {
    public long part1() {
        return solveProperly(readLines());
    }

    public long solveProperly(Stream<String> lines) {
        // Read the grid.
        final var grid = Grid.fromLines(lines, Square::from);
        // We need to do graph sorting.
        final var remainingNodes = grid.allPoints().collect(Collectors.toCollection(HashSet::new));
        // First find the nodes that are have distance 0.
        final var endNodes = grid.allPoints()
            .filter(point -> grid.get(point).distance instanceof Distance.Known known && known.steps() == 0)
            .collect(Collectors.toSet());
        final Stack<Set<Point>> stack = new Stack<>();

        stack.push(endNodes);
        remainingNodes.removeAll(endNodes);

        // When we find the set that contains our Start node, then that's the distance.
        return solveProperly(grid, remainingNodes, stack);
    }

    private long solveProperly(Grid<Square> grid, HashSet<Point> remaining, Stack<Set<Point>> stack) {
        // Then for each iteration, find the nodes that lead into the previous set.
        // Remove them from the full set.

        final var last = stack.peek();
        final var previous = (stack.size() > 1) ? stack.elementAt(stack.size() - 2) : Set.of();
//        System.out.printf("== %d ==\n", stack.size());
//        grid.printGrid(point -> {
//            final var height = grid.get(point).height;
//            return
//                (last.contains(point) ? Term.BG_GREEN :
//                    (previous.contains(point) ? Term.BG_YELLOW : "")) +
//                String.format("%c", height) +
//                Term.RESET;
//        });
//        System.out.println();

        // Find all the nodes that lead into the points into `last`.
        final var newSet = remaining.stream()
            .filter(p1 -> last.stream().anyMatch(p2 -> hasEdgeTo(grid, p1, p2)))
            .collect(Collectors.toSet());
        System.out.printf("New nodes: %s\n", newSet);
        // If those nodes include the start node, return `taken.size()`
        if (newSet.stream().anyMatch(p -> grid.get(p).distance instanceof Distance.Start)) {
            return stack.size();
        }
        // else, recurse
        remaining.removeAll(newSet);
        stack.push(newSet);
        return solveProperly(grid, remaining, stack);
    }

    private boolean hasEdgeTo(Grid<Square> grid, Point p1, Point p2) {
        return (Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y()) == 1)
         && grid.get(p2).height <= grid.get(p1).height + 1;
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
