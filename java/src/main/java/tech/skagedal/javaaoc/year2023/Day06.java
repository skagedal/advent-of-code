package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.math.Longs;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Wait For It"
)
public class Day06 {
    public long part1(AdventContext context) {
        final var lines = context.lines().toList();
        String times = lines.getFirst();
        String distances = lines.getLast();
        return runGames(times, distances);
    }

    public long part2(AdventContext context) {
        final var lines = context.lines().toList();
        String times = lines.getFirst().replaceAll("\\s+", "");
        String distances = lines.getLast().replaceAll("\\s+", "");
        return runGames(times, distances);
    }

    private static long runGames(String times, String distances) {
        var games = Streams.zip(
            Longs.inString(times),
            Longs.inString(distances)
        ).map(Game::from).toList();
        return games.stream().mapToLong(Game::countWays)
            .reduce((x, y) -> x * y)
            .orElseThrow();
    }

    record Game(long time, long distanceRecord) {
        public static Game from(Tuple2<Long, Long> tuple) {
            return new Game(tuple.value1(), tuple.value2());
        }

        long countWays() {
            var sum = 0;
            for (int i = 1; i < time; i++) {
                if ((time - i) * i > distanceRecord) {
                    sum++;
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day06(), """
            Time:      7  15   30
            Distance:  9  40  200""");
        AdventOfCodeRunner.run(new Day06());
    }
}
