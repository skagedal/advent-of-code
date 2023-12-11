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

        grid.printGrid(p -> grid.get(p) ? "#" : ".");

        var points = grid.allPoints().filter(grid::get).toList();
        return Streams.allPairs(points)
            .mapToLong(tuple -> shortestPath(tuple.value1(), tuple.value2()))
            .sum();
    }

    private long shortestPath(Point p1, Point p2) {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }

    private Grid<Boolean> readAndExpandGrid(AdventContext context) {
        return expand(Grid.fromLines(context.lines(), c -> c == '#'));
    }

    private static Grid<Boolean> expand(Grid<Boolean> grid) {
        return expandHorizontally(expandVertically(grid));
    }

    private static Grid<Boolean> expandHorizontally(Grid<Boolean> grid) {
        return expandVertically(grid.invert()).invert();
    }

    private static Grid<Boolean> expandVertically(Grid<Boolean> grid) {
        List<List<Boolean>> theList = grid.allLines()
            .flatMap(lineStream -> {
                var line = lineStream.map(grid::get).toList();
                if (line.stream().noneMatch(Boolean::booleanValue)) {
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

    public long part2(AdventContext context) {
        return 0;
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
