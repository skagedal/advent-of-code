package tech.skagedal.javaaoc;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.Streams;

public class Year2022_Day01 extends Year2022Day {
    public long part1() {
        return Streams.splitting(getLines(), String::isBlank)
            .map(this::sumLines)
            .max(Long::compareTo)
            .orElseThrow();
    }

    public long part2() {
        return Streams.splitting(getLines(), String::isBlank)
            .map(this::sumLines)
            .sorted(Collections.reverseOrder())
            .limit(3)
            .mapToLong(Long::longValue)
            .sum();
    }

    private Stream<String> getLines() {
        return readLines("day01_input.txt");
    }

    private Long sumLines(List<String> strings) {
        return strings.stream().mapToLong(Long::parseLong).sum();
    }
}
