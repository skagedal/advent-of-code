package tech.skagedal.javaaoc.year2022;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AocDay;

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
                    IntStream.iterate(row, i -> i),
                    IntStream.iterate(0, i -> i < width, i -> i + 1)
                );
                markVisible(
                    IntStream.iterate(row, i -> i),
                    IntStream.iterate(width - 1, i -> i >= 0, i -> i - 1)
                );
            }
            for (var column = 0; column < width; column++) {
                markVisible(
                    IntStream.iterate(0, i -> i < height, i -> i + 1),
                    IntStream.iterate(column, i -> i)
                );
                markVisible(
                    IntStream.iterate(height - 1, i -> i >= 0, i -> i - 1),
                    IntStream.iterate(column, i -> i)
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
