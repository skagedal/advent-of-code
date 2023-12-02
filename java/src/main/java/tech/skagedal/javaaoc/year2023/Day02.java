package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.regex.Patterns;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;

@AdventOfCode(
    description = "Cube Conundrum"
)
public class Day02 {
    public long part1(AdventContext context) {
        return context.lines()
            .map(Day02::parseGame)
            .filter(Game::isPossible)
            .mapToLong(Game::id)
            .sum();
    }

    public long part2(AdventContext context) {
        return context.lines()
            .map(Day02::parseGame)
            .mapToLong(Game::power)
            .sum();
    }

    static Pattern GAME_PATTERN = Pattern.compile("Game (\\d+): (.*)");
    static Pattern RED_PATTERN = Pattern.compile("(\\d+) red");
    static Pattern GREEN_PATTERN = Pattern.compile("(\\d+) green");
    static Pattern BLUE_PATTERN = Pattern.compile("(\\d+) blue");
    
    static Game parseGame(String line) {
        final var match = Patterns.match(GAME_PATTERN, line);
        return new Game(Long.parseLong(match.group(1)), parseReveals(match.group(2)));
    }

    private static List<Reveal> parseReveals(String string) {
        return Arrays.stream(string.split(";\\s*"))
            .map(Day02::parseReveal)
            .toList();
    }

    private static Reveal parseReveal(String string) {
        return new Reveal(
            numberOfCubes(string, RED_PATTERN),
            numberOfCubes(string, GREEN_PATTERN),
            numberOfCubes(string, BLUE_PATTERN)
        );
    }

    private static Long numberOfCubes(String string, Pattern pattern) {
        return Patterns.find(pattern, string).map(r -> Long.parseLong(r.group(1))).orElse(0L);
    }


    record Reveal(
        long red,
        long green,
        long blue
    ) {
        long power() {
            return red * green * blue;
        }
    }

    record Game(
        long id,
        List<Reveal> reveals
    ) {
        boolean isPossible() {
            return reveals.stream().allMatch(reveal ->
                reveal.red <= 12 && reveal.green <= 13 && reveal.blue <= 14);
        }

        long power() {
            return maxReveal().power();
        }

        Reveal maxReveal() {
            return new Reveal(
                getMaxForComponent(Reveal::red),
                getMaxForComponent(Reveal::green),
                getMaxForComponent(Reveal::blue)
            );
        }

        private long getMaxForComponent(ToLongFunction<Reveal> componentPicker) {
            return reveals.stream().mapToLong(componentPicker).max().orElse(0);
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day02(), """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""");
        AdventOfCodeRunner.run(new Day02());
    }
}
