package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Sets;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.string.Strings;

@AdventOfCode
public class Day06 {
    public long part1(AdventContext context) {
        return solveFunctional(context, 4);
    }

    public long part2(AdventContext context) {
        return solveFunctional(context, 14);
    }

    private int solveFunctional(AdventContext context, int packetSize) {
        return Streams.enumerated(Streams.splittingFixedSizeOverlapping(context.chars().boxed(), packetSize, packetSize - 1))
            .filter(tuple -> Sets.newHashSet(tuple.value()).size() == packetSize)
            .findFirst().orElseThrow()
            .number() + packetSize;
    }

    private int solveImperative(AdventContext context, int packetSize) {
        final var line = context.lines().findFirst().orElseThrow();
        for (var i = 0; i < line.length() - packetSize; i++) {
            if (Strings.toSet(line.substring(i, i + packetSize)).size() == packetSize) {
                return i + packetSize;
            }
        }
        throw new IllegalStateException("no solution");
    }
}
