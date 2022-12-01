package tech.skagedal.javaaoc;

import java.util.List;
import java.util.stream.LongStream;
import tech.skagedal.javaaoc.tools.Iterators;
import tech.skagedal.javaaoc.tools.Streams;

public class Year2022_Day01 extends Year2022Day {
    public long part1() {
        final var groups = Streams.fromIterator(Iterators.splitting(
            readLines("day01_input.txt").iterator(),
            String::isBlank));
        return groups
            .map(this::sumLines)
            .max(Long::compareTo)
            .orElseThrow();
    }

    private Long sumLines(List<String> strings) {
        return strings.stream().mapToLong(Long::parseLong).sum();
    }

    public long part2() {
        return 0;
    }
}
