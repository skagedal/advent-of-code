package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.geom.Grid;
import tech.skagedal.javaaoc.tools.geom.Point;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.Collections;
import java.util.List;

@AdventOfCode(
    description = "Cosmic Expansion"
)
public class Day11 {
    public long part1(AdventContext context) {
        return solveWithExpansionCoefficient(context, 2L);
    }

    public long part2(AdventContext context) {
        return solveWithExpansionCoefficient(context, 1_000_000L);
    }

    private long solveWithExpansionCoefficient(AdventContext context, long expansionCoefficient) {
        var grid = readAndExpandGrid(context, expansionCoefficient);

        var points = grid.allPoints().filter(p -> grid.get(p).isGalaxy).toList();
        return Streams.allPairs(points)
            .mapToLong(tuple -> shortestPath(grid, tuple.value1(), tuple.value2()))
            .sum();
    }

    private long shortestPath(Grid<Tile> grid, Point p1, Point p2) {
        long sum = 0;
        final var minx = Math.min(p1.x(), p2.x());
        final var maxx = Math.max(p1.x(), p2.x());
        final var miny = Math.min(p1.y(), p2.y());
        final var maxy = Math.max(p1.y(), p2.y());
        for (var x = minx; x < maxx; x++) {
            sum += grid.get(new Point(x, miny)).size;
        }
        for (var y = miny; y < maxy; y++) {
            sum += grid.get(new Point(maxx, y)).size;
        }
        return sum;
    }

    record Tile(boolean isGalaxy, long size) {}

    private Grid<Tile> readAndExpandGrid(AdventContext context, long expansionCoefficient) {
        var initialGrid = Grid.fromLines(context.lines(), c -> new Tile(c == '#', 1));
        return expand(initialGrid, expansionCoefficient);
    }

    private static Grid<Tile> expand(Grid<Tile> grid, long expansionCoefficient) {
        return expandHorizontally(expandVertically(grid, expansionCoefficient), expansionCoefficient);
    }

    private static Grid<Tile> expandHorizontally(Grid<Tile> grid, long expansionCoefficient) {
        return expandVertically(grid.invert(), expansionCoefficient).invert();
    }

    private static Grid<Tile> expandVertically(Grid<Tile> grid, long expansionCoefficient) {
        List<List<Tile>> theList = grid.allLines()
            .map(lineStream -> {
                var line = lineStream.map(grid::get).toList();
                if (line.stream().noneMatch(Tile::isGalaxy)) {
                    return Collections.nCopies(line.size(), new Tile(false, expansionCoefficient));
                } else {
                    return line;
                }
            })
            .toList();

        return new Grid<>(theList);
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day11(), """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....""");
        AdventOfCodeRunner.run(new Day11());
    }

}
