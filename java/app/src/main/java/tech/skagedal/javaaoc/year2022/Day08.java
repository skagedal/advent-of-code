package tech.skagedal.javaaoc.year2022;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.IntStreams;

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
                markVisible(
                    IntStreams.always(row),
                    IntStreams.rangeClosed(0, width - 1, 1)
                );
                markVisible(
                    IntStreams.always(row),
                    IntStreams.rangeClosed(width - 1, 0, -1)
                );
            }
            for (var column = 0; column < width; column++) {
                markVisible(
                    IntStreams.rangeClosed(0, height - 1, 1),
                    IntStreams.always(column)
                );
                markVisible(
                    IntStreams.rangeClosed(height - 1, 0, -1),
                    IntStreams.always(column)
                );
            }
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
    }
}
