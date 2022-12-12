package tech.skagedal.javaaoc.year2022;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.format.Term;
import tech.skagedal.javaaoc.tools.linear.Grid;
import tech.skagedal.javaaoc.tools.linear.Point;

@AdventOfCode
public class Day12 extends AocDay {
    public boolean shouldLog = false;

    public long part1() {
        return solve(readLines(), square -> square.squareType == SquareType.START);
    }

    public long part2() {
        return solve(readLines(), square -> square.height == Square.LOWEST_HEIGHT);
    }

    public long solve(Stream<String> lines, Predicate<Square> isStartingPoint) {
        // Read the grid.
        final var grid = Grid.fromLines(lines, Square::from);
        // We need to do graph sorting.
        final var remainingNodes = grid.allPoints().collect(Collectors.toCollection(HashSet::new));
        // First find the nodes that are have distance 0.
        final var endNodes = grid.allPoints()
            .filter(point -> grid.get(point).squareType() == SquareType.END)
            .collect(Collectors.toSet());
        final Stack<Set<Point>> stack = new Stack<>();

        stack.push(endNodes);
        remainingNodes.removeAll(endNodes);

        // When we find the set that contains our Start node, then that's the distance.
        return solve(grid, remainingNodes, stack, isStartingPoint);
    }

    private long solve(Grid<Square> grid, HashSet<Point> remaining, Stack<Set<Point>> stack, Predicate<Square> isStartingPoint) {
        // Then for each iteration, find the nodes that lead into the previous set.
        // Remove them from the full set.

        final var last = stack.peek();
        final var previous = (stack.size() > 1) ? stack.elementAt(stack.size() - 2) : Set.of();
        if (shouldLog) {
            printState(grid, stack, last, previous);
        }

        // Find all the nodes that lead into the points in `last`.
        final var newSet = remaining.stream()
            .filter(p1 -> last.stream().anyMatch(p2 -> hasEdgeTo(grid, p1, p2)))
            .collect(Collectors.toSet());

        // If the starting point is there, we're done.
        if (newSet.stream().map(grid::get).anyMatch(isStartingPoint)) {
            return stack.size();
        }

        remaining.removeAll(newSet);
        stack.push(newSet);
        return solve(grid, remaining, stack, isStartingPoint);
    }

    private boolean hasEdgeTo(Grid<Square> grid, Point p1, Point p2) {
        return (Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y()) == 1)
         && grid.get(p2).height <= grid.get(p1).height + 1;
    }

    private enum SquareType {
        START, END, NORMAL
    }

    private record Square(int height, SquareType squareType) {
        private static final int LOWEST_HEIGHT = 'a';
        private static final int HEIGHEST_HEIGHT = 'z';

        static Square from(int integer) {
            return fromIntWithStartHeight(LOWEST_HEIGHT).apply(integer);
        }

        public static IntFunction<Square> fromIntWithStartHeight(int startHeight) {
            return integer -> switch (integer) {
                case 'S' -> new Square(startHeight, SquareType.START);
                case 'E' -> new Square(HEIGHEST_HEIGHT, SquareType.END);
                default -> new Square(integer, SquareType.NORMAL);
            };
        }
    }

    private static void printState(Grid<Square> grid, Stack<Set<Point>> stack, Set<Point> last, Set<?> previous) {
        System.out.printf("== %d ==\n", stack.size());
        grid.printGrid(point -> {
            final var height = grid.get(point).height;
            return
                (last.contains(point) ? Term.BG_GREEN :
                    (previous.contains(point) ? Term.BG_YELLOW : "")) +
                    String.format("%c", height) +
                    Term.RESET;
        });
        System.out.println();
    }
}
