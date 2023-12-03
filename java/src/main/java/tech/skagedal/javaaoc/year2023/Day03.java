package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.geom.Grid;
import tech.skagedal.javaaoc.tools.geom.Point;
import tech.skagedal.javaaoc.tools.geom.Vector;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AdventOfCode(
    description = "Gear Ratios"
)
public class Day03 {
    public long part1(AdventContext context) {
        var grid = Grid.fromLines(context.lines(), i -> i);
        return findNumbers(grid)
            .filter(span -> hasNeighbouringSymbol(grid, span))
            .mapToLong(span -> makeNumber(grid, span))
            .sum();
    }

    public long part2(AdventContext context) {
        var grid = Grid.fromLines(context.lines(), i -> i);
        var possibleGears = new HashMap<Point, Set<Span>>();
        for (var numberSpan : Streams.iterate(findNumbers(grid))) {
            for (var possibleGear : Streams.iterate(findPossibleGears(grid, numberSpan))) {
                var set = possibleGears.getOrDefault(possibleGear, new HashSet<>());
                set.add(numberSpan);
                possibleGears.put(possibleGear, set);
            }
        }
        return possibleGears.values().stream()
            .filter(spans -> spans.size() == 2)
            .mapToLong(spans -> spans.stream()
                    .mapToLong(span -> makeNumber(grid, span))
                    .reduce(1, (x, y) -> x * y))
            .sum();
    }

    private Stream<Point> findPossibleGears(Grid<Integer> grid, Span span) {
        return neighbouringPoints(span)
            .filter(grid::isInBounds)
            .filter(point -> grid.get(point) == '*');
    }

    private static long makeNumber(Grid<Integer> grid, Span span) {
        var numeric = Character.getNumericValue(grid.get(span.begin()));
        if (span.begin().equals(span.end())) {
            return numeric;
        } else {
            var one = new Vector(1, 0);
            return makeNumber(grid, new Span(span.begin().plus(one), span.end())) + numeric * (long)Math.pow(10, span.end().x() - span.begin().x());
        }
    }

    private boolean hasNeighbouringSymbol(Grid<Integer> grid, Span span) {
        return neighbouringPoints(span)
            .filter(grid::isInBounds)
            .anyMatch(point -> isSymbol(grid.get(point)));
    }

    private boolean isSymbol(Integer integer) {
        return !Character.isDigit(integer) && integer != '.';
    }

    private Stream<Point> neighbouringPoints(Span span) {
        var y = span.begin().y();
        return Stream.concat(
            Stream.concat(
                IntStream.range(span.begin.x() - 1, span.end.x() + 2).mapToObj(x -> new Point(x, y - 1)),
                IntStream.range(span.begin.x() - 1, span.end.x() + 2).mapToObj(x -> new Point(x, y + 1))
            ),
            Stream.of(
                new Point(span.begin().x() - 1, y),
                new Point(span.end().x() + 1, y)
            )
        );
    }

    Stream<Span> findNumbers(Grid<Integer> grid) {
        return grid.allLines().flatMap(line -> findNumbersInLine(grid, line));
    }

    private Stream<Span> findNumbersInLine(Grid<Integer> grid, Stream<Point> line) {
        return Streams.make(channel -> {
            Point begin = null, end = null;
            for (var point : Streams.iterate(line)) {
                var character = grid.get(point);
                if (Character.isDigit(character)) {
                    if (begin == null) {
                        begin = point;
                    }
                    end = point;
                } else {
                    if (begin != null) {
                        channel.yield(new Span(begin, end));
                        begin = null;
                        end = null;
                    }
                }
            }
            if (begin != null) {
                channel.yield(new Span(begin, end));
            }
        });
    }

    record Span(
        Point begin,
        Point end
    ) {
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day03(), """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..""");
        AdventOfCodeRunner.run(new Day03());
    }
}
