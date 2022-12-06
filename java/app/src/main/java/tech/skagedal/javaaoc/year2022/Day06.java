package tech.skagedal.javaaoc.year2022;

import java.util.HashSet;
import java.util.stream.IntStream;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

public class Day06 extends AocDay {
    public long part1() {
        return solvePartWithPacketSize(4);
    }

    public long part2() {
        return solvePartWithPacketSize(14);
    }

    private int solvePartWithPacketSize(int packetSize) {
        final var line = readLines().findFirst().orElseThrow();
        return Streams.zip(
                Streams.splittingFixedSizeOverlapping(line.chars().boxed(), packetSize, packetSize - 1),
                IntStream.iterate(0, i -> i + 1).boxed())
            .filter(tuple -> new HashSet<>(tuple.value1()).size() == packetSize)
            .findFirst().orElseThrow()
            .value2() + packetSize;
    }

}
