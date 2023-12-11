package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.geom.Grid;
import tech.skagedal.javaaoc.tools.geom.Point;
import tech.skagedal.javaaoc.tools.streamsetc.Lists;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.List;
import java.util.stream.Stream;

@AdventOfCode(
    description = ""
)
public class Day11 {
    public long part1(AdventContext context) {
        var grid = readAndExpandGrid(context);

        var points = grid.allPoints().filter(p -> grid.get(p).isGalaxy).toList();
        return Streams.allPairs(points)
            .mapToLong(tuple -> shortestPath(tuple.value1(), tuple.value2()))
            .sum();
    }

    public long part2(AdventContext context) {
        return 0;
    }


    private long shortestPath(Point p1, Point p2) {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }

    record Tile(boolean isGalaxy, long size) {}

    private Grid<Tile> readAndExpandGrid(AdventContext context) {
        return expand(Grid.fromLines(context.lines(), c -> new Tile(c == '#', 1)));
    }

    private static Grid<Tile> expand(Grid<Tile> grid) {
        return expandHorizontally(expandVertically(grid));
    }

    private static Grid<Tile> expandHorizontally(Grid<Tile> grid) {
        return expandVertically(grid.invert()).invert();
    }

    private static Grid<Tile> expandVertically(Grid<Tile> grid) {
        List<List<Tile>> theList = grid.allLines()
            .flatMap(lineStream -> {
                var line = lineStream.map(grid::get).toList();
                if (line.stream().noneMatch(Tile::isGalaxy)) {
                    return Stream.of(line, line);
                } else {
                    return Stream.of(line);
                }
            })
            .toList();

        return new Grid<>(
            theList
        );
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
