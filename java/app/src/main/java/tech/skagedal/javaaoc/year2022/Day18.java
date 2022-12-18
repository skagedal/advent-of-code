package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.linear.Point3D;

public class Day18 {
    public long part1(AdventContext context) {
        final var points = context.lines()
            .map(Point3D::parseString)
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

    class AdjacentCounted {
        private final Point3D point;
        private int unconnected = 6;

        AdjacentCounted(Point3D point) {
            this.point = point;
        }
    }

    public static void main(String[] args) {
//        runExample();
        AdventOfCodeRunner.run(new Day18());
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
