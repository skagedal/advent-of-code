package tech.skagedal.javaaoc.year2015;

import java.util.List;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.math.Longs;

@AdventOfCode(
    description = "I Was Told There Would Be No Math"
)
public class Day02 {
    public long part1(AdventContext context) {
        return context.lines()
            .mapToLong(line -> wrappingNeeded(Longs.inString(line).toList()))
            .sum();
    }

    private long wrappingNeeded(List<Long> dims) {
        final var sides = List.of(
            dims.get(0) * dims.get(1),
            dims.get(0) * dims.get(2),
            dims.get(1) * dims.get(2)
        );
        return sides.stream().mapToLong(side -> side * 2).sum() +
            sides.stream().mapToLong(Long::longValue).min().orElseThrow();
    }
}
