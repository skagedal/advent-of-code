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
    // Mixing the file means that we move the numbers in the order that they originally appear in the file. We move them
    // by as many position as the value of the number - i.e. a 3 should be moved 3 steps to the right
    //
    //  | 1| 2|-3| 3|-2| 0| 4
    //   ^^
    //
    //  | 2| 1|-3| 3|-2| 0| 4
    //   ^^
    //
    //  | 1|-3| 2| 3|-2| 0| 4
    //      ^^
    //
    // Here comes the tricky bit. Imagine that it twice:
    //
    //    0  1  2  3  4  5  6  0  1  2  3  4  5  6
    //  | 1|-3| 2| 3|-2| 0| 4| 1|-3| 2| 3|-2| 0| 4
    //                           ^^
    //
    // The -3 should be moved so that it is at position 5. It is now at position 1.
    // (1 - 3) % 7 == -2 % 7
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
        // The array that we will mix
        final var mixArray = lines.mapToLong(Long::parseLong).toArray();
        final var n = mixArray.length;

        // Array of positions that we will iterate through
        final var positionArray = IntStream.range(0, n).toArray();

        // Lookback array - for each number in the mixarray, what was it's initial position?
        final var initialPositionArray = IntStream.range(0, n).toArray();

        return new Mixer(context, mixArray, positionArray, initialPositionArray);
    }

    static class Mixer {
        final AdventContext context;
        final long[] mixArray;
        final int[] positionArray;
        final int[] lookbackArray;
        final int n;

        Mixer(AdventContext context, long[] mixArray, int[] positionArray, int[] lookbackArray) {
            this.context = context;
            this.mixArray = mixArray;
            this.positionArray = positionArray;
            this.lookbackArray = lookbackArray;
            this.n = mixArray.length;
            if (positionArray.length != mixArray.length || lookbackArray.length != mixArray.length) {
                throw new RuntimeException("Arrays should have the same length");
            }
        }

        void run() {
            if (context.explain()) {
                System.out.println("Initial arrangement:");
                printArr(mixArray);
                System.out.println();
            }

            for (var i = 0; i < n; i++) {
//                System.out.printf("mixing %d out of %d...\n", i, n);
                final var src = positionArray[i];
                final var valueToMove = mixArray[src];
                final var dest = findDestination(src, valueToMove);
                travel(src, dest);
                if (context.explain()) {
                    System.out.printf("%d moves from position %d to position %d\n", valueToMove, src, dest);
                    printArr(mixArray);
                    System.out.println();
                }
            }
        }

        private long findDestination(int src, long valueToMove) {
            if (valueToMove != (valueToMove % n)) {
                System.out.printf("valueToMove: %d, valueToMove mod n: %d\n", valueToMove, valueToMove % n);
            }

            return src + valueToMove;
//            return src + (valueToMove % n);
        }

        private void travel(long src, long dest) {
            final var delta = Long.signum(dest - src);
            for (var i = src; i != dest; i += delta) {
                var j = i + delta;

                var ic = clamp(i, n);
                var jc = clamp(j, n);
                // Swap places of i and j
                positionArray[lookbackArray[ic]] = jc;
                positionArray[lookbackArray[jc]] = ic;
                swap(lookbackArray, ic, jc);
                swap(mixArray, ic, jc);
            }
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
