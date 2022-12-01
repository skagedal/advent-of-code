package tech.skagedal.javaaoc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.Longs;
import tech.skagedal.javaaoc.tools.Streams;

public class Year2022_Day01 extends Year2022Day {
    public long part1() {
        return Streams.splitting(getLines().map(Longs::parseOptional), Optional::isEmpty, Optional::get)
            .mapToLong(this::sumLongs)
            .max()
            .orElseThrow();
    }

    public long part2() {
        return Streams.splitting(getLines().map(Longs::parseOptional), Optional::isEmpty, Optional::get)
            .map(this::sumLongs)
            .sorted(Collections.reverseOrder())
            .limit(3)
            .mapToLong(Long::longValue)
            .sum();
    }

    private Stream<String> getLines() {
        return readLines("day01_input.txt");
    }

    private Long sumLongs(List<Long> longs) {
        return longs.stream().mapToLong(Long::longValue).sum();
    }
}
