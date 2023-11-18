package tech.skagedal.javaaoc.tools.geom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid3D<T> {
    private final List<List<List<T>>> grid;
    private final Point3D origin;
    private final Point3D end;
    private final Size3D size;

    private Grid3D(List<List<List<T>>> grid, Point3D origin, Size3D size) {
        this.grid = grid;
        this.origin = origin;
        this.size = size;
        this.end = origin.plus(size.toVector());
    }

    public Point3D getOrigin() {
        return origin;
    }

    public static <T> Grid3D<T> enclosing(Stream<Point3D> points, int margin, Function<Point3D, T> mapper) {
        return fromBounds(Rectangle3D.enclosing(points, margin), mapper);
    }

    private static <T> Grid3D<T> fromBounds(Rectangle3D rect, Function<Point3D, T> mapper) {
        final var origin = rect.origin();
        final var end = origin.plus(rect.size().toVector());

        int startX = origin.x();
        int startY = origin.y();
        int startZ = origin.y();
        int endX = end.x() - 1;
        int endY = end.y() - 1;
        int endZ = end.z() - 1;

        final var grid = IntStream.rangeClosed(startZ, endZ).boxed()
            .map(z -> IntStream.rangeClosed(startY, endY).boxed()
                .map(y -> IntStream.rangeClosed(startX, endX).mapToObj(x ->
                    mapper.apply(new Point3D(x, y, z)))
                    .toList())
                .toList())
            .toList();

        return new Grid3D<>(grid, origin, rect.size());
    }

    public boolean isInBounds(Point3D point) {
        return point.x() >= origin.x()
            && point.x() < end.x()
            && point.y() >= origin.y()
            && point.y() < end.y()
            && point.z() >= origin.z()
            && point.z() < end.z();
    }

    public T get(Point3D point) {
        return grid
            .get(point.z() - origin.z())
            .get(point.y() - origin.y())
            .get(point.x() - origin.x());
    }

    public void flood(Point3D point, BiPredicate<Point3D, T> testAndSet) {
        var set = new HashSet<Point3D>();
        set.add(point);

        while (!set.isEmpty()) {
            final var p = set.iterator().next();
            set.remove(p);
            if (testAndSet.test(p, get(p))) {
                for (var v : SIX_DIRECTIONS) {
                    final var p2 = p.plus(v);
                    if (isInBounds(p2)) {
                        set.add(p2);
                    }
                }
            }
        }
    }

    public Stream<Point3D> allPoints() {
        return pointsFrom(origin, new Vector3D(1, 0, 0))
            .flatMap(p1 -> pointsFrom(p1, new Vector3D(0, 1, 0)))
            .flatMap(p2 -> pointsFrom(p2, new Vector3D(0, 0, 1)));
    }

    private Stream<Point3D> pointsFrom(Point3D point, Vector3D direction) {
        return Stream.iterate(point, this::isInBounds, direction::addTo);
    }

    private static List<Vector3D> SIX_DIRECTIONS = sixDirections().toList();

    public static Stream<Vector3D> sixDirections() {
        return Stream.of(
            new Vector3D(1, 0 ,0),
            new Vector3D(-1, 0, 0),
            new Vector3D(0, 1, 0),
            new Vector3D(0, -1, 0),
            new Vector3D(0, 0, 1),
            new Vector3D(0, 0, -1)
        );
    }
}
