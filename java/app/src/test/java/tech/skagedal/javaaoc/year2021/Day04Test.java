package tech.skagedal.javaaoc.year2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class Day04Test {
    @Test
    void part1() {
        final var answer = new Day04().part1();

        assertEquals(63424, answer);
    }

    @Test
    void part2() {
        final var answer = new Day04().part2();

        assertEquals(-1, answer);
    }

    @Test
    void parseBoard() {
        var lines = """
            22 13 17 11  0
             8  2 23  4 24
            21  9 14 16  7
             6 10  3 18  5
             1 12 20 15 19""".lines().toList();
        var board = Day04.Board.parse(lines);

        assertEquals(22, board.getValue(0, 0));
        assertEquals(14, board.getValue(2, 2));
        assertEquals(6, board.getValue(3, 0));
    }

    @Test
    void noBingo() {
        var board = new Day04.Board(new int[5][5], new int[][] {
            {0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        });

        assertFalse(board.isBingo());
    }

    @Test
    void bingoRow() {
        var board = new Day04.Board(new int[5][5], new int[][] {
            {0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        });

        assertTrue(board.isBingo());
    }

    @Test
    void bingoColumn() {
        var board = new Day04.Board(new int[5][5], new int[][] {
            {0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1},
            {0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1}
        });

        assertTrue(board.isBingo());
    }

    @Test
    void sumOfUnmarkedNumbers() {
        var board = new Day04.Board(
            new int[][] {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 0},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}
            },
            new int[][] {
                {0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
            });

        assertEquals(10, board.sumOfUnmarkedNumbers());
    }
}