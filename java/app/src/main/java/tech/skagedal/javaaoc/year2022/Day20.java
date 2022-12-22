package tech.skagedal.javaaoc.year2022;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;

@AdventOfCode(
    description = "Grove Positioning System"
)
public class Day20 {
    public long part1(AdventContext context) {
        final var mixer = loadMixer(context, context.lines());
        mixer.run();
        return mixer.findGroveCoordinates();
    }

    public long part2(AdventContext context) {
        final var mixer = loadMixer(context, context.lines());
        mixer.applyDecryptionKey(811589153);
        for (var i = 0; i < 10; i++) {
            System.out.printf("Run %d...\n", i);
            mixer.run();
        }
        return mixer.findGroveCoordinates();
    }

    private static Mixer loadMixer(AdventContext context, Stream<String> lines) {
        return new Mixer(context.explain(), lines.mapToLong(Long::parseLong).toArray());
    }

    public static class Mixer {
        final boolean explain;

        // The array that we will mix
        final long[] mixArray;

        // Array of positions that we will iterate through
        final int[] positionArray;

        // Lookback array - for each number in the mixarray, what was it's initial position?
        final int[] lookbackArray;
        final int n;

        public Mixer(boolean explain, long[] mixArray) {
            this(
                explain,
                mixArray,
                IntStream.range(0, mixArray.length).toArray(),
                IntStream.range(0, mixArray.length).toArray()
            );
        }

        Mixer(boolean explain, long[] mixArray, int[] positionArray, int[] lookbackArray) {
            this.explain = explain;
            this.mixArray = mixArray;
            this.positionArray = positionArray;
            this.lookbackArray = lookbackArray;
            this.n = mixArray.length;
            if (positionArray.length != mixArray.length || lookbackArray.length != mixArray.length) {
                throw new RuntimeException("Arrays should have the same length");
            }
        }

        void run() {
            if (explain) {
                System.out.println("Initial arrangement:");
                printArr(mixArray);
                System.out.println();
            }

            for (var i = 0; i < n; i++) {
                final var src = positionArray[i];
                final var valueToMove = mixArray[src];
                final var dest = findDestination(src, valueToMove);
                travel(src, dest);
                if (explain) {
                    System.out.printf("%d moves from position %d to position %d\n", valueToMove, src, dest);
                    printArr(mixArray);
                    System.out.println();
                }
            }
        }

        private long findDestination(int src, long valueToMove) {
            // This works for part 1.
            return src + valueToMove;

            // This should work as well, and make part 2 effective. But it doesn't work. Why?
            // return src + (valueToMove % n);
        }

        private void travel(long src, long dest) {
            final var increment = Long.signum(dest - src);
            for (var i = src; i != dest; i += increment) {
                var j = i + increment;

                swapValues(clamp(i, n), clamp(j, n));
            }
        }

        private void swapValues(int posA, int posB) {
            swap(positionArray, lookbackArray[posA], lookbackArray[posB]);
            swap(lookbackArray, posA, posB);
            swap(mixArray, posA, posB);
        }

        public long findGroveCoordinates() {
            int i = 0;
            for (; i < n; i++) {
                if (mixArray[i] == 0) {
                    break;
                }
            }
            long i1 = mixArray[clamp(i + 1000, n)];
            long i2 = mixArray[clamp(i + 2000, n)];
            long i3 = mixArray[clamp(i + 3000, n)];
            return i1 + i2 + i3;
        }

        public void applyDecryptionKey(long decryptionKey) {
            for (var i = 0; i < n; i++) {
                mixArray[i] *= decryptionKey;
            }
        }
    }

    private static void swap(int[] array, int a, int b) {
        var temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private static void swap(long[] array, int a, int b) {
        var temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private static int clamp(long i, int n) {
        int clamped = (int)(i % n);
        return (clamped < 0) ? clamped + n : clamped;
    }

    static void printArr(int[] array) {
        System.out.printf("%s\n", Arrays.stream(array).mapToObj(Integer::toString).collect(Collectors.joining(", ")));
    }

    static void printArr(long[] array) {
        System.out.printf("%s\n", Arrays.stream(array).mapToObj(Long::toString).collect(Collectors.joining(", ")));
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day20(), """
            1
            2
            -3
            3
            -2
            0
            4""");
        AdventOfCodeRunner.run(new Day20());
    }
}
