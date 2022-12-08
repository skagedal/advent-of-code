package tech.skagedal.javaaoc.year2022;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

@AdventOfCode
public class Day08 extends AocDay {
    public long part1() {
        final var forest = Forest.read(readLines());
        forest.markVisible();
        return forest.countVisible();
    }

    public long part2() {
        final var forest = Forest.read(readLines());
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

        @Override
        public String toString() {
            return "" + height + (visible ? "!" : " ");
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
            List<List<Tree>> trees = lines
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
                markVisible(pointsFrom(new Point(row, 0), Vector.ROW_FORWARD));
                markVisible(pointsFrom(new Point(row, width - 1), Vector.ROW_BACKWARD));
            }
            for (var column = 0; column < width; column++) {
                markVisible(pointsFrom(new Point(0, column), Vector.COLUMN_FORWARD));
                markVisible(pointsFrom(new Point(height - 1, column), Vector.COLUMN_BACKWARD));
            }
        }

        private void markVisible(Stream<Point> coordinates) {
            var maxHeight = -1;
            for (var tree : Streams.toIterable(coordinates.map(this::getTree))) {
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
            return Stream.of(Vector.ROW_FORWARD, Vector.ROW_BACKWARD, Vector.COLUMN_FORWARD, Vector.COLUMN_BACKWARD)
                .mapToLong(direction -> scenicScoreOneDirection(point, direction))
                .reduce(1, (a, b) -> a * b);
        }

        private long scenicScoreOneDirection(Point point, Vector direction) {
            final var treeHeight = getTree(point).height;
            var score = 0;
            for (var tree : Streams.toIterable(pointsFrom(point, direction).skip(1).map(this::getTree))) {
                score++;
                if (tree.height >= treeHeight) {
                    break;
                }
            }
            return score;
        }

        // Common helpers

        private Stream<Point> allPoints() {
            return pointsFrom(new Point(0, 0), Vector.COLUMN_FORWARD)
                .flatMap(point -> pointsFrom(point, Vector.ROW_FORWARD));
        }

        private Stream<Point> pointsFrom(Point point, Vector direction) {
            return Stream.iterate(point, this::isInBounds, direction::addTo);
        }

        private boolean isInBounds(Point point) {
            return point.row() >= 0 && point.row() < height && point.column() >= 0 && point.column() < width;
        }

        private Tree getTree(Point point) {
            return trees.get(point.row).get(point.column);
        }
    }

    record Point(int row, int column) { }
    record Vector(int row, int column) {
        static final Vector ROW_FORWARD = new Vector(0, 1);
        static final Vector ROW_BACKWARD = new Vector(0, -1);
        static final Vector COLUMN_FORWARD = new Vector(1, 0);
        static final Vector COLUMN_BACKWARD = new Vector(-1, 0);

        Point addTo(Point point) {
            return new Point(point.row() + row, point.column() + column);
        }
    }
}
