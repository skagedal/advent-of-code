package tech.skagedal.javaaoc.year2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode
public class Day04 {
    public long part1(AdventContext context) {
        final var calledNumbers = context.lines().findFirst()
            .map(Day04::parseCalledNumbers)
            .orElseThrow();
        final var boards = Streams.splitting(context.lines().skip(2), String::isBlank)
            .map(Board::parse).toList();

        for (var number : calledNumbers) {
            for (var board : boards) {
                board.mark(number);
                if (board.isBingo()) {
                    return board.sumOfUnmarkedNumbers() * number;
                }
            }
        }
        throw new IllegalStateException("no answer found");
    }

    // This is really ugly
    public long part2(AdventContext context) {
        final var calledNumbers = context.lines().findFirst()
            .map(Day04::parseCalledNumbers)
            .orElseThrow();
        final var boards = new ArrayList<>(Streams.splitting(context.lines().skip(2), String::isBlank)
            .map(Board::parse).toList());
        Board lastBingoBoard = null;
        int lastNumber = 0;

        for (var number : calledNumbers) {
            for (var board : new ArrayList<>(boards)) {
                board.mark(number);
                if (board.isBingo()) {
                    lastBingoBoard = board;
                    lastNumber = number;
                    boards.remove(board);
                }
            }
        }
        if (lastBingoBoard == null) {
            throw new IllegalStateException("no answer found");
        }
        return lastBingoBoard.sumOfUnmarkedNumbers() * lastNumber;
    }

    private static List<Integer> parseCalledNumbers(String s) {
        return Arrays.stream(s.split(",")).map(Integer::parseInt).toList();
    }

    private static Pattern NUMBERS = Pattern.compile("\\d+");

    static class Board {
        int[][] number;
        int[][] marked;

        public Board(int[][] number, int[][] marked) {
            this.number = number;
            this.marked = marked;
        }

        static Board parse(List<String> lines) {
            return new Board(
                lines.stream()
                    .map(Board::parseRow)
                    .toArray(int[][]::new),
                new int[5][5]
            );
        }

        private static int[] parseRow(String line) {
            return NUMBERS.matcher(line).results().mapToInt(result -> Integer.parseInt(result.group())).toArray();
        }

        public int getValue(int row, int column) {
            return number[row][column];
        }

        public void mark(int value) {
            for (var i = 0; i < marked.length; i++) {
                for (var j = 0; j < marked[i].length; j++) {
                    if (number[i][j] == value) {
                        mark(i, j);
                    }
                }
            }
        }

        public void mark(int row, int column) {
            marked[row][column] = 1;
        }

        public boolean isBingo() {
            return rows(marked).anyMatch(row -> row.allMatch(i -> i == 1))
                || columns(marked).anyMatch(column -> column.allMatch(i -> i == 1));
        }

        private static Stream<IntStream> rows(int[][] matrix) {
            return IntStream.range(0, 5).mapToObj(row -> row(matrix, row));
        }

        private static Stream<IntStream> columns(int[][] matrix) {
            return IntStream.range(0, 5).mapToObj(column -> column(matrix, column));
        }

        private static IntStream row(int[][] matrix, int row) {
            return Arrays.stream(matrix[row], 0, 5);
        }

        private static IntStream column(int[][] matrix, int column) {
            return IntStream.range(0, 5).map(row -> matrix[row][column]);
        }

        public long sumOfUnmarkedNumbers() {
            return Streams.zip(rows(number), rows(marked))
                .flatMap(tuple -> Streams.zip(tuple.value1().boxed(), tuple.value2().boxed()).
                    map(tuple2 -> tuple2.value1() * (1 - tuple2.value2())))
                .mapToInt(Integer::intValue)
                .sum();
        }
    }
}
