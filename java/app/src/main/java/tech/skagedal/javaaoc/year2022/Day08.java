package tech.skagedal.javaaoc.year2022;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.linear.Point;
import tech.skagedal.javaaoc.tools.linear.Vector;

@AdventOfCode
public class Day08 {
    public long part1(AdventContext context) {
        final var forest = Forest.read(context.lines());
        forest.markVisible();
        return forest.countVisible();
    }

    public long part2(AdventContext context) {
        final var forest = Forest.read(context.lines());
        return forest
            .allPoints()
            .mapToLong(forest::scenicScore)
            .max()
            .orElseThrow();
    }

    static class Tree {
        final int height;
        boolean visible = false;

        Tree(int height) {
            this.height = height;
        }

        public boolean isVisible() {
            return visible;
        }
    }

    static class Forest {
        final List<List<Tree>> trees;
        private final int height;
        private final int width;

        Forest(List<List<Tree>> trees, int height, int width) {
            this.trees = trees;
            this.height = height;
            this.width = width;
        }

        // Parsing

        static Forest read(Stream<String> lines) {
            final var trees = lines
                .map(Forest::readLine)
                .toList();
            return new Forest(trees, trees.size(), trees.get(0).size());
        }

        private static List<Tree> readLine(String line) {
            return line.chars().map(i -> i - '0').mapToObj(Tree::new).toList();
        }

        // Part 1

        void markVisible() {
            for (var row = 0; row < height; row++) {
                markVisible(pointsFrom(new Point(0, row), ROW_FORWARD));
                markVisible(pointsFrom(new Point(width - 1, row), ROW_BACKWARD));
            }
            for (var column = 0; column < width; column++) {
                markVisible(pointsFrom(new Point(column, 0), COLUMN_FORWARD));
                markVisible(pointsFrom(new Point(column, height - 1), COLUMN_BACKWARD));
            }
        }

        private void markVisible(Stream<Point> coordinates) {
            var maxHeight = -1;
            for (var tree : Streams.iterate(coordinates.map(this::getTree))) {
                if (tree.height > maxHeight) {
                    tree.visible = true;
                    maxHeight = tree.height;
                }
            }
        }

        long countVisible() {
            return trees.stream()
                .flatMap(Collection::stream)
                .filter(Tree::isVisible)
                .count();
        }

        // Part 2

        public long scenicScore(Point point) {
            return Stream.of(ROW_FORWARD, ROW_BACKWARD, COLUMN_FORWARD, COLUMN_BACKWARD)
                .mapToLong(direction -> scenicScoreOneDirection(point, direction))
                .reduce(1, (a, b) -> a * b);
        }

        private long scenicScoreOneDirection(Point point, Vector direction) {
            final var treeHeight = getTree(point).height;
            var score = 0;
            for (var tree : Streams.iterate(pointsFrom(point, direction).skip(1).map(this::getTree))) {
                score++;
                if (tree.height >= treeHeight) {
                    break;
                }
            }
            return score;
        }

        // Common helpers

        private Stream<Point> allPoints() {
            return pointsFrom(new Point(0, 0), COLUMN_FORWARD)
                .flatMap(point -> pointsFrom(point, ROW_FORWARD));
        }

        private Stream<Point> pointsFrom(Point point, Vector direction) {
            return Stream.iterate(point, this::isInBounds, direction::addTo);
        }

        private boolean isInBounds(Point point) {
            return point.y() >= 0 && point.y() < height && point.x() >= 0 && point.x() < width;
        }

        private Tree getTree(Point point) {
            return trees.get(point.y()).get(point.x());
        }
    }

    private static final Vector ROW_FORWARD = new Vector(1, 0);
    private static final Vector ROW_BACKWARD = new Vector(-1, 0);
    private static final Vector COLUMN_FORWARD = new Vector(0, 1);
    private static final Vector COLUMN_BACKWARD = new Vector(0, -1);


}
