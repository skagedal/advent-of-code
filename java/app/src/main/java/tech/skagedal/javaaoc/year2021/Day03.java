package tech.skagedal.javaaoc.year2021;

import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.Year2021Day;

public class Day03 extends Year2021Day {
    public long part1() {
        final var occurrences = getLines()
            .map(Occurrences::fromLine)
            .reduce(Occurrences::plus)
            .orElseThrow();
        
        var epsilon = occurrences.toRate(integer -> integer > 0);
        var gamma = occurrences.toRate(integer -> integer < 0);
        return epsilon * gamma;
    }

    public long part2() {
        return 0;
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
    }

    private Stream<String> getLines() {
        return readLines("day03_input.txt");
    }
}
