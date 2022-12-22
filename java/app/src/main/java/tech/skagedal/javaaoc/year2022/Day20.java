package tech.skagedal.javaaoc.year2022;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        // The array that we will mix
        final var mixArray = context.lines().mapToInt(Integer::parseInt).toArray();
        final var n = mixArray.length;

        // Array of positions that we will iterate through
        final var positionArray = IntStream.range(0, n).toArray();

        // Lookback array - for each number in the sortarray, what was it's initial position?
        final var initialPositionArray = IntStream.range(0, n).toArray();

        new Mixer(context, mixArray, positionArray, initialPositionArray).run();

//        firstAttempt(context, mixArray, n, positionArray, initialPositionArray);

        // Now, calculate grove coordinates.
        // Find position of value 0:

        int i = 0;
        for (; i < n; i++) {
            if (mixArray[i] == 0) {
                break;
            }
        }
        System.out.printf("Position of 0 is %d (doublechecking: %d)\n", i, mixArray[i]);
        int i1 = mixArray[clamp(i + 1000, n)];
        int i2 = mixArray[clamp(i + 2000, n)];
        int i3 = mixArray[clamp(i + 3000, n)];
        var sum = i1 + i2 + i3;
        System.out.printf("%d + %d + %d == %d\n", i1, i2, i3, sum);
        return sum;
    }

    static class Mixer {
        final AdventContext context;
        final int[] mixArray;
        final int[] positionArray;
        final int[] lookbackArray;
        final int n;

        Mixer(AdventContext context, int[] mixArray, int[] positionArray, int[] lookbackArray) {
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

//            lotsOfCasesAttempt();

            for (var i = 0; i < n; i++) {
                final var src = positionArray[i];
                final var valueToMove = mixArray[src];
                final var dest = src + valueToMove;
//                final var dest = findDestination(src, valueToMove);
                coolTravel(src, dest);
                if (context.explain()) {
                    System.out.printf("%d moves from position %d to position %d\n", valueToMove, src, dest);
                    printArr(mixArray);
                    System.out.println();
                }
            }
        }

        private int findDestination(int src, int valueToMove) {
            final var x = (src + valueToMove) % n;
            if (x < 0) {
                return n + x - 1;
            } else {
                return x;
            }
        }

        private void lotsOfCasesAttempt() {
            for (var i = 0; i < n; i++) {
                final var src = positionArray[i];
                final var valueToMove = mixArray[src];
                final var dest = src + valueToMove;
                if (dest == src) {
                    if (context.explain()) {
                        System.out.println("[case 1]");
                        System.out.printf("%d does not move:\n", valueToMove);
                        printArr(mixArray);
                        System.out.println();
                    }
                } else if (dest > 0 && dest <= n-1) {
                    // Trivial case
                    if (context.explain()) {
                        System.out.println("[case 2]");
                        System.out.printf("%d moves between %d and %d:\n", valueToMove, mixArray[dest], mixArray[dest + 1]);
                    }
                    travel(src, dest);
                    if (context.explain()) {
                        printArr(mixArray);
                        System.out.println();
                    }
                } else if (dest <= 0) {
                    final var modifiedDest = n + (dest % n) - 1;
                    if (context.explain()) {
                        System.out.println("[case 3]");
                        System.out.printf("%d moves between %d and %d:\n", valueToMove, mixArray[modifiedDest],
                            mixArray[(modifiedDest + 1) % n]);
                    }
                    travel(src, modifiedDest);
                    if (context.explain()) {
                        printArr(mixArray);
                        System.out.println();
                    }
                } else if (dest >= n) {
                    final var modifiedDest = (dest % n);
                    if (context.explain()) {
                        System.out.println("[case 4]");
                        System.out.printf("%d moves between %d and %d:\n", valueToMove, mixArray[modifiedDest],
                            mixArray[(modifiedDest + 1) % n]);
                    }
                    travel(src, modifiedDest);
                    if (context.explain()) {
                        printArr(mixArray);
                        System.out.println();
                    }
                } else {
                    throw new IllegalStateException(String.format("NOT IMPLEMENTED - dest == %d, n == %d", dest, n));
                }
            }
        }

        private void travel(int src, int dest) {
            if (src < 0) throw new IllegalStateException("src must be at least 0");
            if (src >= n) throw new IllegalStateException("src must be less than n");
            if (dest < 0) throw new IllegalStateException("dest must be at least 0");
            if (dest >= n) throw new IllegalStateException("dest must be less than n");
            final var delta = Integer.signum(dest - src);
            for (var i = src; i != dest; i += delta) {
                var j = i + delta;
                // Swap places of i and j
                positionArray[lookbackArray[i]] += delta;
                positionArray[lookbackArray[j]] -= delta;
                swap(lookbackArray, i, j);
                swap(mixArray, i, j);
            }
        }

        private void coolTravel(int src, int dest) {
            final var delta = Integer.signum(dest - src);
            for (var i = src; i != dest; i += delta) {
                var j = i + delta;

                var ic = clamp(i, n);
                var jc = clamp(j, n);
                // Swap places of i and j
                addClamped(positionArray, lookbackArray[ic], delta, n);
                addClamped(positionArray, lookbackArray[jc], -delta, n);
                swap(lookbackArray, ic, jc);
                swap(mixArray, ic, jc);
            }
        }

    }

    private static void addClamped(int[] arr, int pos, int delta, int n) {
        arr[pos] = clamp(arr[pos] + delta, n);
    }

    private void firstAttempt(AdventContext context, int[] mixArray, int n, int[] positionArray, int[] initialPositionArray) {
        for (var i = 0; i < n; i++) {
            final var pos = positionArray[i];
            if (context.explain()) {
                System.out.printf("== Mixing %d: at position %d - %d\n", i, pos, mixArray[pos]);
            }
            var moveLength1 = mixArray[pos] % n;

            int moveDest;
            if (pos + moveLength1 >= n) {
                moveDest = clamp(pos + moveLength1 + 1, n);
            } else if (pos + moveLength1 <= 0) {
                moveDest = clamp(pos + moveLength1 - 1, n);
            } else {
                moveDest = clamp(pos + moveLength1, n);
            }

            var moveLength = moveDest - pos;
            if (context.explain()) {
                System.out.printf("Moving from position %d to position %d - i.e. diff of %d\n", pos, moveDest, moveLength);
            }
            if (moveLength > 0) {
                for (var j = 0; j < moveLength; j++) {
                    int aPos = clamp((pos + j), n);
                    int bPos = clamp((pos + j + 1), n);
                    positionArray[initialPositionArray[aPos]] = clamp(positionArray[initialPositionArray[aPos]] + 1, n);
                    positionArray[initialPositionArray[bPos]] = clamp(positionArray[initialPositionArray[bPos]] - 1, n);
                    swap(mixArray, aPos, bPos);
                    swap(initialPositionArray, aPos, bPos);
                }
            } else if (moveLength < 0) {
                for (var j = 0; j > moveLength; j--) {
                    int aPos = clamp((pos + j), n);
                    int bPos = clamp((pos + j - 1), n);
                    positionArray[initialPositionArray[aPos]] = clamp(positionArray[initialPositionArray[aPos]] - 1, n);
                    positionArray[initialPositionArray[bPos]] = clamp(positionArray[initialPositionArray[bPos]] + 1, n);
                    swap(mixArray, aPos, bPos);
                    swap(initialPositionArray, aPos, bPos);
                }
            }
            if (context.explain()) {
                printState("Mix array", mixArray);
                printState("Position ", positionArray);
                printState("Initial  ", initialPositionArray);
                System.out.println();
            }
        }
    }

    // Like I do now:
    // [(-1), 2, 3] => [3, 2, -1]
    // We want:
    // [(-1), 2, 3] => [2, -1, 3]



    // Like I do now
    // [4, 5, (1)] => [1, 5, 4]
    // We want
    // [4, 5, (1)] => [4, 1, 5]

    // Or:
    // [3, 4, 5, 6, (1)] => [3, 1, 4, 5, 6]

    private static void swap(int[] array, int a, int b) {
        var temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private static int clamp(int i, int n) {
        int clamped = i % n;
        return (clamped < 0) ? clamped + n : clamped;
    }

    void printState(String name, int[] array) {
        System.out.printf("%s: [%s]\n", name, Arrays.stream(array).mapToObj(Integer::toString).collect(Collectors.joining(", ")));
    }

    static void printArr(int[] array) {
        System.out.printf("%s\n", Arrays.stream(array).mapToObj(Integer::toString).collect(Collectors.joining(", ")));
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
