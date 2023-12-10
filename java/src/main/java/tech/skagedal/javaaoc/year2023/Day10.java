package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.geom.Grid;
import tech.skagedal.javaaoc.tools.geom.Point;

import static java.util.function.Predicate.not;

@AdventOfCode(
    description = "Pipe Maze"
)
public class Day10 {
    public long part1(AdventContext context) {
        var grid = Grid.fromLines(context.lines(), Tile::fromChar);
        grid.printGrid(point -> grid.get(point).printable());
        var start = grid.allPoints().filter(point -> grid.get(point) == Tile.START).findFirst().orElseThrow();
        Point prev = new Point(-1, -1);
        var next = start;
        var moves = 0;
        do {
            var tmp = next;
            next = findConnecting(grid, next, prev);
            moves++;
            prev = tmp;
        } while (!next.equals(start));
        return moves / 2;
    }

    public long part2(AdventContext context) {
        return 0;
    }

    private Point findConnecting(Grid<Tile> grid, Point point, Point excluded) {
        var tile = grid.get(point);

        return Grid.fourDirections()
            .map(point::plus)
            .filter(grid::isInBounds)
            .filter(not(excluded::equals))
            .filter(p -> connectsTo(grid, point, p))
            .findFirst()
            .orElseThrow();
    }

    private boolean connectsTo(Grid<Tile> grid, Point point1, Point point2) {
        var vec = point2.minus(point1);
        var tile1 = grid.get(point1);
        var tile2 = grid.get(point2);
        return switch (vec.dx()) {
            case 1 -> tile1.matchesEast(tile2);
            case -1 -> tile2.matchesEast(tile1);
            case 0 -> switch (vec.dy()) {
                case -1 -> tile1.matchesNorth(tile2);
                case 1 -> tile2.matchesNorth(tile1);
                default -> throw new IllegalStateException();
            };
            default -> throw new IllegalStateException();
        };
    }

    enum Tile {
        VERTICAL,
        HORIZONTAL,
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_WEST,
        SOUTH_EAST,
        GROUND,
        START;

        static Tile fromChar(int character) {
            return switch (character) {
                case '|' -> VERTICAL;
                case '-' -> HORIZONTAL;
                case 'L' -> NORTH_EAST;
                case 'J' -> NORTH_WEST;
                case '7' -> SOUTH_WEST;
                case 'F' -> SOUTH_EAST;
                case '.' -> GROUND;
                case 'S' -> START;
                default -> throw new IllegalArgumentException();
            };
        }

        public String printable() {
            return switch (this) {
                case VERTICAL -> "│";
                case HORIZONTAL -> "─";
                case NORTH_EAST -> "└";
                case NORTH_WEST -> "┘";
                case SOUTH_WEST -> "┐";
                case SOUTH_EAST -> "┌";
                case GROUND -> ".";
                case START -> "◇";
            };
        }

        public boolean matchesEast(Tile other) {
            return switch (this) {
                case VERTICAL, NORTH_WEST, SOUTH_WEST, GROUND ->
                    false;
                case START, HORIZONTAL, NORTH_EAST, SOUTH_EAST ->
                    other == NORTH_WEST || other == SOUTH_WEST || other == HORIZONTAL || other == START;
            };
        }

        public boolean matchesNorth(Tile other) {
            return switch (this) {
                case HORIZONTAL, SOUTH_WEST, SOUTH_EAST, GROUND ->
                    false;
                case START, VERTICAL, NORTH_WEST, NORTH_EAST ->
                    other == SOUTH_WEST || other == SOUTH_EAST || other == VERTICAL || other == START;
            };
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day10(), """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ""");
        AdventOfCodeRunner.run(new Day10());
    }
}
