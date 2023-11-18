package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.geom.Grid3D;
import tech.skagedal.javaaoc.tools.geom.Point3D;
import tech.skagedal.javaaoc.tools.visualize.VisualizeDay18;

@AdventOfCode(
    description = "Boiling Boulders"
)
public class Day18 {
    public long part1(AdventContext context) {
        return numberOfSurfaces(context.lines().map(Point3D::parseString));
    }

    public long part2(AdventContext context) {
        final var points = context.lines().map(Point3D::parseString).collect(Collectors.toSet());
        final var grid = Grid3D.enclosing(points.stream(), 1, p -> new AtomicBoolean(false));

        grid.flood(grid.getOrigin(), (p, isThisCubeletSet) -> {
            if (isThisCubeletSet.get()) return false;
            if (points.contains(p)) return false;
            isThisCubeletSet.set(true);
            return true;
        });

        // The unmarked points are the solid rock
        final var solidRockPoints = grid.allPoints()
            .filter(p -> !grid.get(p).get())
            .toList();

        return numberOfSurfaces(solidRockPoints.stream());
    }

    private static int numberOfSurfaces(Stream<Point3D> point3DStream) {
        final var points = point3DStream
            .sorted(Comparator.comparing(Point3D::x))
            .map(AdjacentCounted::new)
            .collect(Collectors.toCollection(ArrayList::new));

        final var n = points.size();
        for (var i = 0; i < n; i++) {
            final var a = points.get(i);
            final var ap = a.point;
            for (var j = i + 1; j < n && (points.get(j).point.x() - a.point.x()) < 2; j++) {
                final var b = points.get(j);
                final var bp = b.point;
                if (Math.abs(ap.x()-bp.x()) + Math.abs(ap.y()-bp.y()) + Math.abs(ap.z()-bp.z()) == 1) {
                    a.unconnected--;
                    b.unconnected--;
                }
            }
        }

        return points.stream().mapToInt(ac -> ac.unconnected).sum();
    }

    static class AdjacentCounted {
        private final Point3D point;
        private int unconnected = 6;

        AdjacentCounted(Point3D point) {
            this.point = point;
        }
    }

    private static void visualize(AdventContext context) {
        final var points = context.lines().map(Point3D::parseString).toList();

        VisualizeDay18.writeObjFile(points, "out.obj");
    }

    public static void main(String[] args) {
        runExample();
    }

    private static void runExample() {
        AdventOfCodeRunner.example(
            new Day18(),
            """
                2,2,2
                1,2,2
                3,2,2
                2,1,2
                2,3,2
                2,2,1
                2,2,3
                2,2,4
                2,2,6
                1,2,5
                3,2,5
                2,1,5
                2,3,5"""
            );
    }
}
