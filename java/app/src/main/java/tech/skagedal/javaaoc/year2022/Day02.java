package tech.skagedal.javaaoc.year2022;

import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.aoc.AdventOfCode;

@AdventOfCode
public class Day02 extends AocDay {
    public long part1() {
        return readLines()
            .map(Round::fromLine)
            .mapToLong(Round::getScore)
            .sum();
    }

    public long part2() {
        return readLines()
            .map(RoundWithOutcome::fromLine)
            .map(RoundWithOutcome::toRound)
            .mapToLong(Round::getScore)
            .sum();
    }

    public record Round(Move opponentsMove, Move yourMove) {

        public long getScore() {
            return yourMove.score + (((yourMove.score - opponentsMove.score) + 4) % 3) * 3;
        }
        public static Round fromLine(String line) {
            return new Round(Move.fromCharacter(line.charAt(0)), Move.fromCharacter(line.charAt(2)));
        }

    }
    public record RoundWithOutcome(Move opponentsMove, Outcome outcome) {
        public static RoundWithOutcome fromLine(String line) {
            return new RoundWithOutcome(Move.fromCharacter(line.charAt(0)), Outcome.fromCharacter(line.charAt(2)));
        }

        public Round toRound() {
            return new Round(opponentsMove, outcome.yourMoveWhenOpponentsMove(opponentsMove));
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

    public enum Outcome {
        LOSE, DRAW, WIN;

        public static Outcome fromCharacter(char character) {
            return switch (character) {
                case 'X' -> LOSE;
                case 'Y' -> DRAW;
                case 'Z' -> WIN;
                default -> throw new IllegalArgumentException("Unknown outcome: " + character);
            };
        }

        public Move yourMoveWhenOpponentsMove(Move opponentsMove) {
            return switch (this) {
                case LOSE -> switch (opponentsMove) {
                    case ROCK -> Move.SCISSORS;
                    case PAPER -> Move.ROCK;
                    case SCISSORS -> Move.PAPER;
                };
                case DRAW -> opponentsMove;
                case WIN -> switch (opponentsMove) {
                    case ROCK -> Move.PAPER;
                    case PAPER -> Move.SCISSORS;
                    case SCISSORS -> Move.ROCK;
                };
            };
        }
    }
}
