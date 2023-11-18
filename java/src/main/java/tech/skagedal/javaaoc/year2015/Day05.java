package tech.skagedal.javaaoc.year2015;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Doesn't He Have Intern-Elves For This?"
)
public class Day05 {
    public long part1(AdventContext context) {
        return context.lines().filter(Day05::isNiceInPart1).count();
    }

    public long part2(AdventContext context) {
        return context.lines().filter(Day05::isNiceInPart2).count();
    }

    private static Set<Tuple2<Integer, Integer>> naughtyPairs = Set.of(
        new Tuple2<>((int) 'a', (int) 'b'),
        new Tuple2<>((int) 'c', (int) 'd'),
        new Tuple2<>((int) 'p', (int) 'q'),
        new Tuple2<>((int) 'x', (int) 'y')
    );

    public static boolean isNiceInPart1(String string) {
        return
            (string.chars().filter(Day05::isVowel).limit(3).count() == 3) &&
                pairs(string).anyMatch(Day05::isSame) &&
                pairs(string).noneMatch(Day05::isNaughtyPair);
    }

    private static boolean isNiceInPart2(String string) {
        return
            hasRepeatingNonOverlappingPair(string) &&
            containsThreeLetterPalindrome(string);
    }

    private static boolean isSame(Tuple2<Integer, Integer> tuple) {
        return Objects.equals(tuple.value1(), tuple.value2());
    }

    private static Stream<Tuple2<Integer, Integer>> pairs(String s) {
        return Streams.splittingToTuple2Overlapping(s.chars().boxed());
    }

    private static boolean isNaughtyPair(Tuple2<Integer, Integer> tuple) {
        return naughtyPairs.contains(tuple);
    }

    private static boolean isVowel(int character) {
        return switch (character) {
            case 'a', 'e', 'i', 'o', 'u' -> true;
            default -> false;
        };
    }

    private static boolean hasRepeatingNonOverlappingPair(String string) {
        for (int i = 0; i < string.length() - 2; i++) {
            final var pair = string.substring(i, i + 2);
            if (string.substring(i + 2).contains(pair)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsThreeLetterPalindrome(String string) {
        for (int i = 0; i < string.length() - 2; i++) {
            if (string.charAt(i) == string.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}
