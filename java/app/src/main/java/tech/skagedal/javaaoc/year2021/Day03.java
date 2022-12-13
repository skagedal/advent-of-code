package tech.skagedal.javaaoc.year2021;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;

@AdventOfCode
public class Day03 {
    public long part1(AdventContext context) {
        final var occurrences = context.lines()
            .map(Occurrences::fromLine)
            .reduce(Occurrences::plus)
            .orElseThrow();
        
        var epsilon = occurrences.toRate(integer -> integer > 0);
        var gamma = occurrences.toRate(integer -> integer < 0);
        return epsilon * gamma;
    }

    public long part2(AdventContext context) {
        final var occurrences = context.lines()
            .map(Occurrences::fromLine)
            .toList();

        long first = part2Rec(0, integer -> integer >= 0, occurrences);
        long second = part2Rec(0, integer -> integer <= 0, occurrences);
        return first * second;
    }

    private long part2Rec(int digit, Predicate<Integer> predicate, List<Occurrences> occurrences) {
        final var sum = occurrences.stream().reduce(Occurrences::plus).orElseThrow();
        final var expected = sum.testDigit(digit, integer -> integer >= 0);
        final var filtered = occurrences.stream().filter(occurrence ->
            occurrence.testDigit(digit, predicate) == expected).toList();
        if (filtered.size() == 1) {
            return filtered.get(0).toRate(integer -> integer > 0);
        } else {
            return part2Rec(digit + 1, predicate, filtered);
        }
    }

    record Occurrences(
        int[] counts
    ) {
        static Occurrences fromLine(String string) {
            var bytes = string.getBytes(StandardCharsets.US_ASCII);
            var counts = new int[bytes.length];
            for (var i = 0; i < counts.length; i++) {
                counts[i] = (((int)bytes[i]) - '0') * 2 - 1;
            }
            return new Occurrences(counts);
        }

        Occurrences plus(Occurrences other) {
            var sum = new int[counts.length];
            for (var i = 0; i < sum.length; i++) {
                sum[i] = counts[i] + other.counts[i];
            }
            return new Occurrences(sum);
        }
        
        long toRate(Predicate<Integer> predicate) {
            long sum = 0;
            for (int count : counts) {
                sum <<= 1;
                if (predicate.test(count)) {
                    sum += 1;
                }
            }
            return sum;
        }

        boolean testDigit(int digit, Predicate<Integer> predicate) {
            return predicate.test(counts[digit]);
        }
    }
}
