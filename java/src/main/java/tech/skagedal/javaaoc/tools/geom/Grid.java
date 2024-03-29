package tech.skagedal.javaaoc.tools.geom;

import com.google.common.base.Functions;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid<T> {
    private final List<List<T>> grid;
    private final int startX;
    private final int startY;
    private final int height;
    private final int width;

    public Grid(List<List<T>> grid) {
        this(grid, 0, 0, grid.get(0).size(), grid.size());
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private Grid(List<List<T>> grid, int startX, int startY, int width, int height) {
        this.grid = grid;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        for (var line : grid) {
            if (line.size() != width) {
                throw new IllegalArgumentException("One line did not have the same width as the others");
            }
        }
    }

    public Size getSize() {
        return new Size(width, height);
    }

    /**
     * Takes a stream of strings where each string represents a row and each character a column of that row.
     */
    public static <T> Grid<T> fromLines(Stream<String> lines, IntFunction<T> mapper) {
        return new Grid<>(lines.map(line -> line.chars().mapToObj(mapper).toList()).toList());
    }

    public static <T> Grid<T> fromBounds(int startX, int endX, int startY, int endY, Function<Point, T> mapper) {
        final var grid = IntStream.rangeClosed(startY, endY)
            .boxed()
            .map(y -> IntStream.rangeClosed(startX, endX).mapToObj(x -> mapper.apply(new Point(x, y))).toList())
            .toList();
        return new Grid<>(grid, startX, startY, endX - startX + 1, endY - startY + 1);
    }

    public static <T> Grid<T> enclosing(Stream<Point> points, Function<Point, T> mapper) {
        final var rect = Rectangle.enclosing(points);
        final var origin = rect.origin();
        final var end = origin.plus(rect.size().toVector());

        return fromBounds(origin.x(), end.x() - 1, origin.y(), end.y()  - 1, mapper);
    }
    public T get(Point point) {
        return grid
            .get(point.y() - startY)
            .get(point.x() - startX);
    }

    public Stream<Point> allPoints() {
        return allLines().flatMap(Functions.identity());
    }

    public Stream<Stream<Point>> allLines() {
        return pointsFrom(new Point(startX, startY), COLUMN_FORWARD)
            .map(point -> pointsFrom(point, ROW_FORWARD));
    }

    private Stream<Point> pointsFrom(Point point, Vector direction) {
        return Stream.iterate(point, this::isInBounds, direction::addTo);
    }

    public boolean isInBounds(Point point) {
        return
            point.y() >= startY && point.y() < (startY + height) &&
                point.x() >= startX && point.x() < (startX + width);
    }

    public Grid<T> invert() {
        var inverted = IntStream.range(0, width).mapToObj(x ->
            IntStream.range(0, height)
                .mapToObj(y -> grid.get(y).get(x)).toList()
        ).toList();
        return new Grid<>(inverted);
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
        allLines().forEach(line -> {
            line.forEach(point -> System.out.printf(formatter.apply(point)));
            System.out.println();
        });
    }
}
