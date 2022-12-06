package tech.skagedal.javaaoc.year2021;

import java.util.List;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

public class Day04 extends AocDay {
    public long part1() {
        final var list = Streams.splitting(readLines(), String::isBlank).toList();
        final var instructions = list.get(0);


        return 0;
    }

    public long part2() {
        return 0;
    }

    static class Board {
        int[][] number;
        boolean[][] marked;

        public Board(int[][] number, boolean[][] marked) {
            this.number = number;
            this.marked = marked;
        }

        static Board parse(List<String> lines) {
            lines.stream()
                .map(line -> line.)
            return new Board(
                new int[5][5],
                new boolean[5][5]
            );
        }
    }
}
