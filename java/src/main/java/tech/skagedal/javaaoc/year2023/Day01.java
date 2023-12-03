package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.regex.Patterns;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.string.ReverseCharSequence;
import tech.skagedal.javaaoc.tools.string.Strings;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static tech.skagedal.javaaoc.tools.string.Strings.reverse;

@AdventOfCode(
    description = "Trebuchet?!"
)
public class Day01 {
    public long part1(AdventContext context) {
        return new Solver(NUMBERS_PART1).solve(context.lines());
    }

    public long part2(AdventContext context) {
        return new Solver(NUMBERS_PART2).solve(context.lines());
    }

    private static class Solver {
        private final Pattern forwardPattern;
        private final Pattern backwardPattern;
        private final Map<String, Long> numberLookup;

        private Solver(List<Number> numbers) {
            this.forwardPattern = buildForwardPattern(numbers);
            this.backwardPattern = buildBackwardPattern(numbers);
            this.numberLookup = numbers.stream().collect(Collectors.toMap(Number::representation, Number::value));
        }

        public long solve(Stream<String> lines) {
            return lines
                .mapToLong(this::sumFirstAndLast)
                .sum();
        }

        private long sumFirstAndLast(String string) {
            var first = numberLookup.get(
                find(forwardPattern, string));
            var last = numberLookup.get(reverse(
                find(backwardPattern, new ReverseCharSequence(string))));
            return first * 10 + last;
        }

        private String find(Pattern pattern, CharSequence charSequence) {
            var matcher = pattern.matcher(charSequence);
            if (matcher.find()) {
                return matcher.group();
            }
            throw new IllegalStateException();
        }

        private static Pattern buildForwardPattern(List<Number> numbers) {
            return buildPattern(numbers.stream().map(Number::representation));
        }

        private static Pattern buildBackwardPattern(List<Number> numbers) {
            return buildPattern(numbers.stream().map(Number::representation).map(Strings::reverse));
        }

        private static Pattern buildPattern(Stream<String> strings) {
            return Pattern.compile(strings.collect(Collectors.joining("|")));
        }
    }

    private record Number(
        String representation,
        long value
    ) { }

    private static final List<Number> ARABIC_DIGITS = IntStream.rangeClosed(0, 9)
        .mapToObj(i -> new Number(Integer.toString(i), i))
        .toList();

    private static final List<Number> WRITTEN_DIGITS = Streams.enumerated(Stream.of(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"))
        .map(stringEnumerated -> new Number(stringEnumerated.value(), stringEnumerated.number() + 1L))
        .toList();

    private static final List<Number> NUMBERS_PART1 = ARABIC_DIGITS;
    private static final List<Number> NUMBERS_PART2 = Stream.concat(ARABIC_DIGITS.stream(), WRITTEN_DIGITS.stream()).toList();

    public static void main(String[] args) {
        AdventOfCodeRunner.run(new Day01());
    }
}
