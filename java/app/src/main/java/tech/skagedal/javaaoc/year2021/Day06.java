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
        return simulateFishGeneration(context, 80);
    }

    // TODO: We need to find the formula instead - this becomes way to big. We should be able to find the expansion
    //   function for each starting value and add them up.
    public long part2(AdventContext context) {
        return simulateFishGeneration2(context, 256);
    }
    public long simulateFishGeneration(AdventContext context, int generations) {
        final var line = context.lines().findFirst().orElseThrow();
        Deque<Long> fish = Arrays.stream(line.split(","))
            .map(Long::parseLong)
            .collect(Collectors.toCollection(LinkedList::new));

        if (context.explain()) {
            System.out.println("Initial state: " + formatFish(fish));
        }
        for (var i = 0; i < generations; i++) {
            fish = generate(fish);
            System.out.printf("After %02d days: %s\n", i + 1, formatFish(fish));
        }
        return fish.size();
    }

    public long simulateFishGeneration2(AdventContext context, int generations) {
        final var line = context.lines().findFirst().orElseThrow();
        List<Long> fish = Arrays.stream(line.split(",")).map(Long::parseLong).toList();

        long[] fisk = new long[9];
        for (var f : fish) {
            fisk[f.intValue() % 9]++;
        }
        for (var i = 0; i < generations; i++) {
            long generating = fisk[0];
            for (var j = 0; j < 8; j++) {
                fisk[j] = fisk[j + 1];
            }
            fisk[8] = generating;
            fisk[6] += generating;
        }
        return Arrays.stream(fisk).sum();
    }

    private String formatFish(Deque<Long> fish) {
        return "(" + fish.size() + ") " +
            fish.stream().map(Object::toString).collect(Collectors.joining(","));
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
        System.out.println("Sol 1: " + new Day06().simulateFishGeneration(AdventContext.fromString("3,4,3,1,2"), 18));
        System.out.println("Sol 2: " + new Day06().simulateFishGeneration2(AdventContext.fromString("3,4,3,1,2"), 18));
//        System.out.println(new Day06().simulateFishGeneration(AdventContext.fromString("6"), 100));
    }
}
