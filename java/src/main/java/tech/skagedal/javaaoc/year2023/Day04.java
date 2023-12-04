package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Ints;
import tech.skagedal.javaaoc.tools.regex.Patterns;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AdventOfCode(
    description = "Scratchcards"
)
public class Day04 {
    public long part1(AdventContext context) {
        return context.lines()
            .map(Card::parse)
            .mapToLong(Card::winningPoints)
            .sum();
    }

    public long part2(AdventContext context) {
        var cards = context.lines().map(Card::parse).toList();
        var count = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            count[i] = 1;
        }
        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            var wins = card.winningCount();
            for (int k = 0; k < wins && i + k + 1 < cards.size(); k++) {
                count[i + k + 1] += count[i];
            }
        }
        return Arrays.stream(count).sum();
    }

    record Card(
        int id,
        Set<Integer> winningNumbers,
        List<Integer> numbers
    ) {
        private static Pattern PATTERN = Pattern.compile("^Card\\s+(\\d+):\\s*([\\d\\s]*)\\|([\\d\\s]*$)");

        public static Card parse(String line) {
            var result = Patterns.match(PATTERN, line);
            return new Card(
                Ints.inString(result.group(1)).findAny().get(),
                Ints.inString(result.group(2)).collect(Collectors.toSet()),
                Ints.inString(result.group(3)).toList()
            );
        }

        public int winningPoints() {
            var count = winningCount();
            return count > 0 ? 1 << (count - 1) : 0;
        }

        private long winningCount() {
            return numbers.stream().filter(winningNumbers::contains).count();
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day04(), """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""");
        AdventOfCodeRunner.run(new Day04());
    }
}
