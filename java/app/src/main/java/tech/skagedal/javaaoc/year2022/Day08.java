package tech.skagedal.javaaoc.year2022;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.IntStreams;
import tech.skagedal.javaaoc.tools.Streams;

public class Day08 extends AocDay {
    public long part1() {
        final var forest = Forest.read(readLines());
        forest.markVisible();
        return forest.countVisible();
    }

    public long part2() {
        final var forest = Forest.read(readLines());
        return forest
            .allCoordinates()
            .mapToLong(forest::calculateScenicScore)
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

        static Forest read(Stream<String> lines) {
            List<List<Tree>> trees = lines
                .map(Forest::readLine)
                .toList();
            return new Forest(trees, trees.size(), trees.get(0).size());
        }

        private static List<Tree> readLine(String line) {
            return line.chars().map(i -> i - '0').mapToObj(Tree::new).toList();
        }

        void markVisible() {
            for (var row = 0; row < height; row++) {
                markVisible(rowForward(row));
                markVisible(rowBackward(row));
            }
            for (var column = 0; column < width; column++) {
                markVisible(columnForward(column));
                markVisible(columnBackward(column));
            }
        }

        private Stream<Coordinates> allCoordinates() {
            return IntStream.range(0, height).boxed().flatMap(this::rowForward);
        }

        private Stream<Coordinates> columnForward(int column) {
            return columnForward(new Coordinates(0, column));
        }

        private Stream<Coordinates> columnForward(Coordinates coordinates) {
            return IntStreams.zip(
                IntStreams.rangeClosed(coordinates.row, height - 1, 1),
                IntStreams.always(coordinates.column()),
                Coordinates::new
            );
        }

        private Stream<Coordinates> columnBackward(int column) {
            return columnBackward(new Coordinates(height - 1, column));
        }

        private Stream<Coordinates> columnBackward(Coordinates coordinates) {
            return IntStreams.zip(
                IntStreams.rangeClosed(coordinates.row(), 0, -1),
                IntStreams.always(coordinates.column()),
                Coordinates::new
            );
        }

        private Stream<Coordinates> rowForward(int row) {
            return rowForward(new Coordinates(row, 0));
        }

        private Stream<Coordinates> rowForward(Coordinates coordinates) {
            return IntStreams.zip(
                IntStreams.always(coordinates.row()),
                IntStreams.rangeClosed(coordinates.column(), width - 1, 1),
                Coordinates::new
            );
        }

        private Stream<Coordinates> rowBackward(int row) {
            return rowBackward(new Coordinates(row, width - 1));
        }

        private Stream<Coordinates> rowBackward(Coordinates coordinates) {
            return IntStreams.zip(
                IntStreams.always(coordinates.row()),
                IntStreams.rangeClosed(coordinates.column(), 0, -1),
                Coordinates::new
            );
        }

        long countVisible() {
            return trees.stream()
                .flatMap(Collection::stream)
                .filter(Tree::isVisible)
                .count();
        }

        private void markVisible(IntStream rows, IntStream columns) {
            final var rowIterator = rows.iterator();
            final var columnIterator = columns.iterator();

            var maxHeight = -1;
            while (rowIterator.hasNext() && columnIterator.hasNext()) {
                var tree = trees.get(rowIterator.next()).get(columnIterator.next());
                if (tree.height > maxHeight) {
                    tree.visible = true;
                    maxHeight = tree.height;
                }
            }
        }

        private void markVisible(Stream<Coordinates> coordinates) {
            var maxHeight = -1;
            for (var tree : Streams.toIterable(coordinates.map(this::getTree))) {
                if (tree.height > maxHeight) {
                    tree.visible = true;
                    maxHeight = tree.height;
                }
            }
        }

        private Tree getTree(Coordinates coordinates) {
            return trees.get(coordinates.row).get(coordinates.column);
        }

        public long calculateScenicScore(Coordinates coordinates) {
            return Stream.of(
                    rowForward(coordinates),
                    rowBackward(coordinates),
                    columnForward(coordinates),
                    columnBackward(coordinates)
                )
                .mapToLong(row -> calculateSubScenicScore(row.map(this::getTree)))
                .reduce(1, (a, b) -> a * b);
        }

        private long calculateSubScenicScore(Stream<Tree> trees) {
            var iter = trees.iterator();
            final var height = iter.next().height;
            var score = 0;
            while (iter.hasNext()) {
                score++;
                if (iter.next().height >= height) {
                    break;
                }
            }
            return score;
        }
    }

    record Coordinates(int row, int column) {}
}
