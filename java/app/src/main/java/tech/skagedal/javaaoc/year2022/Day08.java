package tech.skagedal.javaaoc.year2022;

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
        return 0;
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
                markVisible(rowForwards(row));
                markVisible(rowBackwards(row));
            }
            for (var column = 0; column < width; column++) {
                markVisible(columnForward(column));
                markVisible(columnBackward(column));
            }
        }

        private Stream<Coordinates> columnForward(int column) {
            return columnForward(column, 0);
        }

        private Stream<Coordinates> columnForward(int column, int startRow) {
            return IntStreams.zip(
                IntStreams.rangeClosed(startRow, height - 1, 1),
                IntStreams.always(column),
                Coordinates::new
            );
        }

        private Stream<Coordinates> columnBackward(int column) {
            return columnBackward(column, height - 1);
        }

        private Stream<Coordinates> columnBackward(int column, int startRow) {
            return IntStreams.zip(
                IntStreams.rangeClosed(startRow, 0, -1),
                IntStreams.always(column),
                Coordinates::new
            );
        }

        private Stream<Coordinates> rowForwards(int row) {
            return rowForward(row, 0);
        }

        private Stream<Coordinates> rowForward(int row, int startColumn) {
            return IntStreams.zip(
                IntStreams.always(row),
                IntStreams.rangeClosed(startColumn, width - 1, 1),
                Coordinates::new
            );
        }

        private Stream<Coordinates> rowBackwards(int row) {
            return rowBackwards(row, width - 1);
        }

        private Stream<Coordinates> rowBackwards(int row, int startColumn) {
            return IntStreams.zip(
                IntStreams.always(row),
                IntStreams.rangeClosed(width - 1, 0, -1),
                Coordinates::new
            );
        }

        long countVisible() {
            return trees.stream()
                .flatMap(treeRow -> treeRow.stream())
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

        public void calculateScenicScore(Coordinates coordinates) {
        }

    }

    record Coordinates(int row, int column) {}
}
