package tech.skagedal.javaaoc.year2022;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.math.Longs;

@AdventOfCode
public class Day01 {
    public long part1(AdventContext context) {
        return Streams.splitting(context.lines().map(Longs::parseOptional), Optional::isEmpty, Optional::get)
            .mapToLong(this::sumLongs)
            .max()
            .orElseThrow();
    }

    public long part2(AdventContext context) {
        return Streams.splitting(context.lines().map(Longs::parseOptional), Optional::isEmpty, Optional::get)
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
