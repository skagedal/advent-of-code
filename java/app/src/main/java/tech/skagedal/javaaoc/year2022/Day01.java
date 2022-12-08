package tech.skagedal.javaaoc.year2022;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.Longs;
import tech.skagedal.javaaoc.tools.Streams;

@AdventOfCode
public class Day01 extends AocDay {
    public long part1() {
        return Streams.splitting(readLines().map(Longs::parseOptional), Optional::isEmpty, Optional::get)
            .mapToLong(this::sumLongs)
            .max()
            .orElseThrow();
    }

    public long part2() {
        return Streams.splitting(readLines().map(Longs::parseOptional), Optional::isEmpty, Optional::get)
            .map(this::sumLongs)
            .sorted(Collections.reverseOrder())
            .limit(3)
            .mapToLong(Long::longValue)
            .sum();
    }

    private Long sumLongs(List<Long> longs) {
        return longs.stream().mapToLong(Long::longValue).sum();
    }
}
