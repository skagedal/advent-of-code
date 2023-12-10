package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.format.Term;
import tech.skagedal.javaaoc.tools.geom.Direction4;
import tech.skagedal.javaaoc.tools.geom.Grid;
import tech.skagedal.javaaoc.tools.geom.Point;
import tech.skagedal.javaaoc.tools.geom.Vector;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.HashSet;
import java.util.Set;

import static java.util.function.Predicate.not;

@AdventOfCode(
    description = "Pipe Maze"
)
public class Day10 {
    public long part1(AdventContext context) {
        var grid = Grid.fromLines(context.lines(), Tile::fromChar);
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
        var grid = Grid.fromLines(context.lines(), Tile::fromChar);
        var start = grid.allPoints().filter(point -> grid.get(point) == Tile.START).findFirst().orElseThrow();
        Point firstConnection = null;
        Point prev = new Point(-1, -1);
        var next = start;
        var pointsInLoop = new HashSet<Point>();
        do {
            var tmp = next;
            pointsInLoop.add(next);
            next = findConnecting(grid, next, prev);
            prev = tmp;
            if (firstConnection == null) {
                firstConnection = next;
            }
        } while (!next.equals(start));

        System.out.printf("Start:            %s\n", start);
        System.out.printf("First connection: %s\n", firstConnection);
        System.out.printf("Last connection:  %s\n", prev);
        // Figure out the type of the start tile
        var startVec = firstConnection.minus(prev);
        Tile startTile = tileMatchingVector(startVec);
        System.out.printf("Start tile is:   %s\n", startTile);


        // We make a larger grid because that makes bounds checking simpler.
        // Also we remove all junk tiles.
        var searchGrid = Grid.fromBounds(
            grid.getStartX(),
            grid.getStartX() + grid.getWidth(),
            grid.getStartY(),
            grid.getStartY() + grid.getHeight(),
            point -> pointsInLoop.contains(point)
                ? grid.get(point)
                : Tile.GROUND
        );
        var outsidePoints = new Filler(searchGrid, start, startTile).fill();
        printGrid(searchGrid, pointsInLoop, outsidePoints);

        return (long) searchGrid.getWidth() * searchGrid.getHeight() - pointsInLoop.size() - outsidePoints.size();
    }

    private Tile tileMatchingVector(Vector startVec) {
        switch (startVec.dx()) {
            case 0:
                return Tile.VERTICAL;
            case 1:
                switch (startVec.dy()) {
                    case -1:
                        return Tile.SOUTH_EAST;
                    case 1:
                        return Tile.NORTH_EAST;
                    default:
                        throw new IllegalStateException();
                }
            case -1:
                switch (startVec.dy()) {
                    case -1:
                        return Tile.SOUTH_WEST;
                    case 1:
                        return Tile.NORTH_WEST;
                    default:
                        throw new IllegalStateException();
                }
            default:
                return Tile.HORIZONTAL;
        }

    }

    static class Filler {
        private final Grid<Tile> grid;
        private final Point start;
        private final Tile startTile;
        private final Set<Point> visited = new HashSet<>();
        private final Set<Point> filled = new HashSet<>();

        Filler(Grid<Tile> grid, Point start, Tile startTile) {
            this.grid = grid;
            this.start = start;
            this.startTile = startTile;
        }

        public Set<Point> fill() {
            fillFrom(new Point(grid.getStartX() + grid.getWidth() - 1, 0));
            return filled;
        }

        private void fillFrom(Point point) {
            if (visited.contains(point)) {
                return;
            }
            visited.add(point);
            var tile = getTile(point);
            if (tile == Tile.GROUND) {
                filled.add(point);
            }
            for (var direction : Streams.iterate(Direction4.allFour())) {
                var next = point.plus(direction.vector());
                if (!grid.isInBounds(next)) {
                    continue;
                }
                switch (direction) {
                    case PLUS_X -> {
                        if (tile.allowsMovementOnNorthSide()) {
                            fillFrom(next);
                        }
                    }
                    case MINUS_X -> {
                        if (getTile(next).allowsMovementOnNorthSide()) {
                            fillFrom(next);
                        }
                    }
                    case PLUS_Y -> {
                        if (tile.allowsMovementOnWestSide()) {
                            fillFrom(next);
                        }
                    }
                    case MINUS_Y -> {
                        if (getTile(next).allowsMovementOnWestSide()) {
                            fillFrom(next);
                        }
                    }
                }
            }
        }

        private Tile getTile(Point point) {
            if (point.equals(start)) {
                return startTile;
            }
            return grid.get(point);
        }
    }

    private static void printGrid(Grid<Tile> searchGrid, HashSet<Point> pointsInLoop, Set<Point> outsidePoints) {
        searchGrid.printGrid(point -> {
            if (pointsInLoop.contains(point)) {
                return Term.FG_RED + searchGrid.get(point).printable() + Term.RESET;
            }
            if (outsidePoints.contains(point)) {
                return Term.BG_YELLOW + searchGrid.get(point).printable() + Term.RESET;
            }
            return searchGrid.get(point).printable();
        });
    }

    private Point findConnecting(Grid<Tile> grid, Point point, Point excluded) {
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

        public boolean allowsMovementOnNorthSide() {
            return switch (this) {
                case VERTICAL, NORTH_WEST, NORTH_EAST -> false;
                case START, HORIZONTAL, SOUTH_EAST, SOUTH_WEST, GROUND -> true;
            };
        }

        public boolean allowsMovementOnWestSide() {
            return switch (this) {
                case HORIZONTAL, NORTH_WEST, SOUTH_WEST -> false;
                case START, VERTICAL, SOUTH_EAST, NORTH_EAST, GROUND -> true;
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
        AdventOfCodeRunner.example(new Day10(), """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L""");
        AdventOfCodeRunner.run(new Day10());
    }
}
