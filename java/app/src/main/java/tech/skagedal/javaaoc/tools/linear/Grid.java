package tech.skagedal.javaaoc.tools.linear;

import com.google.common.base.Functions;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.year2022.Day08;

public class Grid<T> {
    private final List<List<T>> grid;
    private final int height;
    private final int width;

    public Grid(List<List<T>> grid) {
        this.grid = grid;
        this.height = grid.size();
        this.width = grid.get(0).size();
        for (var line : grid) {
            if (line.size() != width) {
                throw new IllegalArgumentException("One line did not have the same width as the others");
            }
        }
    }

    public static <T> Grid<T> fromLines(Stream<String> lines, IntFunction<T> mapper) {
        return new Grid<T>(lines.map(line -> line.chars().mapToObj(mapper).toList()).toList());
    }


    public T get(Point point) {
        return grid.get(point.y()).get(point.x());
    }

    public Stream<Point> allPoints() {
        return allPointsInLines().flatMap(Functions.identity());
    }

    private Stream<Stream<Point>> allPointsInLines() {
        return pointsFrom(new Point(0, 0), COLUMN_FORWARD)
            .map(point -> pointsFrom(point, ROW_FORWARD));
    }

    private Stream<Point> pointsFrom(Point point, Vector direction) {
        return Stream.iterate(point, this::isInBounds, direction::addTo);
    }

    public boolean isInBounds(Point point) {
        return point.y() >= 0 && point.y() < height && point.x() >= 0 && point.x() < width;
    }

    public Stream<T> all() {
        return grid.stream().flatMap(Collection::stream);
    }

    public static final Vector ROW_FORWARD = new Vector(1, 0);
    public static final Vector ROW_BACKWARD = new Vector(-1, 0);
    public static final Vector COLUMN_FORWARD = new Vector(0, 1);
    public static final Vector COLUMN_BACKWARD = new Vector(0, -1);

    public static Stream<Vector> fourDirections() {
        return Stream.of(
            ROW_FORWARD,
            ROW_BACKWARD,
            COLUMN_FORWARD,
            COLUMN_BACKWARD
        );
    }

    public void printGrid(Function<Point, String> formatter) {
        allPointsInLines().forEach(line -> {
            line.forEach(point -> System.out.printf(formatter.apply(point)));
            System.out.println();
        });
    }
}
