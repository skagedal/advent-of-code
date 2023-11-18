package tech.skagedal.javaaoc.year2021;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;

@AdventOfCode
public class Day06 {
    public long part1(AdventContext context) {
        return simulateFishGeneration(context.line(), 80);
    }

    public long part2(AdventContext context) {
        return simulateFishGeneration(context.line(), 256);
    }

    public long simulateFishGeneration(String line, int generations) {
        List<Long> initialState = Arrays.stream(line.split(",")).map(Long::parseLong).toList();

        long[] fishState = new long[9];
        for (var f : initialState) {
            fishState[f.intValue() % 9]++;
        }
        for (var i = 0; i < generations; i++) {
            long generating = fishState[0];
            for (var j = 0; j < 8; j++) {
                fishState[j] = fishState[j + 1];
            }
            fishState[8] = generating;
            fishState[6] += generating;
        }
        return Arrays.stream(fishState).sum();
    }
}
