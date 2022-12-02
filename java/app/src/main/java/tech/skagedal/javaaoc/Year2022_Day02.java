package tech.skagedal.javaaoc;

import java.util.stream.Stream;

public class Year2022_Day02 extends Year2022Day {
    public long part1() {
        return getLines()
            .map(Round::fromLine)
            .mapToLong(Round::getScore)
            .sum();
    }

    public long part2() {
        return 0;
    }

    public record Round(Move opponentsMove, Move yourMove) {
        public long getScore() {
            return yourMove.score + (((yourMove.score - opponentsMove.score) + 4) % 3) * 3;
        }

        public static Round fromLine(String line) {
            return new Round(Move.fromCharacter(line.charAt(0)), Move.fromCharacter(line.charAt(2)));
        }
    }
    public enum Move {
        ROCK(1), PAPER(2), SCISSORS(3);

        public final long score;

        Move(long score) {
            this.score = score;
        }

        public static Move fromCharacter(char character) {
            return switch (character) {
                case 'A', 'X' -> Move.ROCK;
                case 'B', 'Y' -> Move.PAPER;
                case 'C', 'Z' -> Move.SCISSORS;
                default -> throw new IllegalArgumentException("Unknown move: " + character);
            };
        }
    }

    private Stream<String> getLines() {
        return readLines("day02_input.txt");
    }
}
