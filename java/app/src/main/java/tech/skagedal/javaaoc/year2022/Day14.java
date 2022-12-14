package tech.skagedal.javaaoc.year2022;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.linear.Grid;
import tech.skagedal.javaaoc.tools.linear.Point;

public class Day14 {

    public static final Point SAND_ORIGIN = new Point(500, 0);

    public long part1(AdventContext context) {
        List<RockPath> rockPaths = context.lines().map(RockPath::fromString).toList();
        final var grid = Grid.enclosing(
            Stream.concat(
                rockPaths.stream().flatMap(rp -> rp.points().stream()),
                Stream.of(SAND_ORIGIN)
            ),
            point -> new Box<>(Contents.EMPTY));

        grid.printGrid(p -> String.format("%03d,%03d ", p.x(), p.y()));
        return 0;
    }

    record RockPath(List<Point> points) {
        static RockPath fromString(String string) {
            return new RockPath(Point.parsePointsInString(string).toList());
        }
    }

    enum Contents { EMPTY, ROCK, SAND }

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
        System.out.println(new Day14().part1(AdventContext.fromString(code)));
    }
}
