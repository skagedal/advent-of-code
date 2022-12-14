package tech.skagedal.javaaoc.year2021;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;

@AdventOfCode
public class Day06 {
    public long part1(AdventContext context) {
        return simulateFishGeneration(context, 80);
    }

    // TODO: We need to find the formula instead - this becomes way to big. We should be able to find the expansion
    //   function for each starting value and add them up.
    public long part2Disabled(AdventContext context) {
        return simulateFishGeneration(context, 256);
    }

    private int simulateFishGeneration(AdventContext context, int generations) {
        final var line = context.lines().findFirst().orElseThrow();
        Deque<Long> fish = Arrays.stream(line.split(","))
            .map(Long::parseLong)
            .collect(Collectors.toCollection(LinkedList::new));

        for (var i = 0; i < generations; i++) {
            fish = generate(fish);
        }
        return fish.size();
    }

    private Deque<Long> generate(Deque<Long> fishes) {
        Deque<Long> nextGeneration = new LinkedList<>();
        Deque<Long> freshFish = new LinkedList<>();
        for (var fish : fishes) {
            fish--;
            if(fish < 0) {
                nextGeneration.addLast(6L);
                freshFish.addLast(8L);
            } else {
                nextGeneration.addLast(fish);
            }
        }
        nextGeneration.addAll(freshFish);
        return nextGeneration;
    }

    public static void main(String[] args) {
        System.out.println(new Day06().part1(AdventContext.fromString("3,4,3,1,2")));
    }
}
