package tech.skagedal.javaaoc.year2021;

import java.util.stream.Stream;
import tech.skagedal.javaaoc.Year2021Day;

public class Day02 extends Year2021Day {
    public long part1() {
        final var position = getLines()
            .map(Command::fromString)
            .reduce(Position.START, Position::move, Position::combine);

        return position.horizontal() * position.depth();
    }

    public long part2() {
        final var positionWithAim = getLines()
            .map(Command::fromString)
            .reduce(PositionWithAim.START, PositionWithAim::move, PositionWithAim::combine);

        return positionWithAim.horizontal() * positionWithAim.depth();
    }

    enum CommandType {
        FORWARD, DOWN, UP;

        static CommandType fromString(String string) {
            return valueOf(string.toUpperCase());
        }
    }
    record Command(
        CommandType type,
        long distance
    ) {
        static Command fromString(String string) {
            final var split = string.split(" ");
            return new Command(CommandType.fromString(split[0]), Long.parseLong(split[1]));
        }
    }

    record Position(
        long horizontal,
        long depth
    ) {
        static final Position START = new Position(0, 0);
        
        Position move(Command command) {
            return switch (command.type) {
                case FORWARD -> new Position(horizontal + command.distance(), depth);
                case DOWN -> new Position(horizontal, depth + command.distance());
                case UP -> new Position(horizontal, depth - command.distance());
            };
        }

        // This doesn't really make sense but Stream.reduce requires it :/
        Position combine(Position p) {
            return new Position(horizontal + p.horizontal, depth + p.depth);
        }
    }

    record PositionWithAim(
        long horizontal,
        long depth,
        long aim
    ) {
        static final PositionWithAim START = new PositionWithAim(0, 0, 0);

        PositionWithAim move(Command command) {
            return switch (command.type) {
                case FORWARD -> new PositionWithAim(horizontal + command.distance(), depth + command.distance() * aim, aim);
                case DOWN -> new PositionWithAim(horizontal, depth, aim + command.distance());
                case UP -> new PositionWithAim(horizontal, depth, aim - command.distance());
            };
        }

        // This doesn't really make sense but Stream.reduce requires it :/
        PositionWithAim combine(PositionWithAim p) {
            return new PositionWithAim(horizontal + p.horizontal, depth + p.depth, aim + p.aim);
        }

    }

    private Stream<String> getLines() {
        return readLines("day02_input.txt");
    }
}
