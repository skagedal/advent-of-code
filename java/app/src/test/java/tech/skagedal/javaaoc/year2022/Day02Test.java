package tech.skagedal.javaaoc.year2022;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Day02Test {
    private final Day02 day = new Day02();

    @Test
    void part1() {
        final var answer = day.part1();

        assertEquals(9759, answer);;
    }

    @Test
    void part2() {
        final var answer = day.part2();

        assertEquals(12429, answer);
    }

    @Test
    void testScoring() {
        assertEquals(8, new Day02.Round(Day02.Move.ROCK, Day02.Move.PAPER).getScore());
        assertEquals(1, new Day02.Round(Day02.Move.PAPER, Day02.Move.ROCK).getScore());
        assertEquals(6, new Day02.Round(Day02.Move.SCISSORS, Day02.Move.SCISSORS).getScore());

        assertEquals(3, new Day02.Round(Day02.Move.ROCK, Day02.Move.SCISSORS).getScore());

        assetScore(4, "A X");
        assetScore(8, "A Y");
        assetScore(3, "A Z");
        assetScore(1, "B X");
        assetScore(5, "B Y");
        assetScore(9, "B Z");
        assetScore(7, "C X");
        assetScore(2, "C Y");
        assetScore(6, "C Z");
    }

    private static void assetScore(int expectedScore, String line) {
        final var round = Day02.Round.fromLine(line);
        final var score = round.getScore();
        assertEquals(expectedScore, score,
            String.format("Line was %s - your move is %s, worth %d points. Their move was %s, so outcome was %d. Total score was %d.",
                line,
                round.yourMove(),
                round.yourMove().score,
                round.opponentsMove(),
                score - round.yourMove().score,
                score
                ));
    }
}