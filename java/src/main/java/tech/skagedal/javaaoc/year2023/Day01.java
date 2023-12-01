package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;

import java.util.List;

@AdventOfCode(
    description = "Trebuchet?!"
)
public class Day01 {
    public long part1(AdventContext context) {
        return context.lines()
            .mapToInt(this::sumFirstAndLastDigit)
            .sum();
    }

    public long part2(AdventContext context) {
        return context.lines()
            .mapToInt(this::sumFirstAndLastNumber)
            .sum();
    }

    private int sumFirstAndLastDigit(String string) {
        var digits = string.codePoints()
            .filter(Character::isDigit)
            .map(Character::getNumericValue)
            .toArray();
        if (digits.length == 0) {
            return 0;
        }
        return digits[0] * 10 + digits[digits.length - 1];
    }

    private int sumFirstAndLastNumber(String string) {
        return findFirst(string) * 10 + findLast(string);
    }

    private int findFirst(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.codePointAt(i))) {
                return Character.getNumericValue(string.codePointAt(i));
            }
            var substr = string.substring(i);
            for (var num : NUMBERS) {
                if (substr.startsWith(num.representation)) {
                    return num.value;
                }
            }
        }
        throw new IllegalStateException();
    }

    private int findLast(String string) {
        for (int i = string.length(); i > 0; i--) {
            if (Character.isDigit(string.codePointAt(i - 1))) {
                return Character.getNumericValue(string.codePointAt(i - 1));
            }
            var substr = string.substring(0, i);
            for (var num : NUMBERS) {
                if (substr.endsWith(num.representation)) {
                    return num.value;
                }
            }
        }
        throw new IllegalStateException();
    }

    private record Number(
        String representation,
        int value
    ) { }

    private static List<Number> NUMBERS = List.of(
        new Number("one", 1),
        new Number("two", 2),
        new Number("three", 3),
        new Number("four", 4),
        new Number("five", 5),
        new Number("six", 6),
        new Number("seven", 7),
        new Number("eight", 8),
        new Number("nine", 9)
    );
    public static void main(String[] args) {
        AdventOfCodeRunner.run(new Day01());
    }
}
