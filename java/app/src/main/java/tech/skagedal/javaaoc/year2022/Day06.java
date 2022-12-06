package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Sets;
import java.util.stream.IntStream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Strings;

public class Day06 extends AocDay {
    public long part1() {
        return solveFunctional(4);
    }

    public long part2() {
        return solveFunctional(14);
    }

    private int solveFunctional(int packetSize) {
        return Streams.zip(
                Streams.splittingFixedSizeOverlapping(readChars().boxed(), packetSize, packetSize - 1),
                IntStream.iterate(0, i -> i + 1).boxed())
            .filter(tuple -> Sets.newHashSet(tuple.value1()).size() == packetSize)
            .findFirst().orElseThrow()
            .value2() + packetSize;
    }

    private int solveImperative(int packetSize) {
        final var line = readLines().findFirst().orElseThrow();
        for (var i = 0; i < line.length() - packetSize; i++) {
            if (Strings.toSet(line.substring(i, i + packetSize)).size() == packetSize) {
                return i + packetSize;
            }
        }
        throw new IllegalStateException("no solution");
    }
}
