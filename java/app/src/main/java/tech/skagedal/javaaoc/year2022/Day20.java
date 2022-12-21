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
    public long part1(AdventContext context) {
        // The array that we will mix
        final var mixArray = context.lines().mapToInt(Integer::parseInt).toArray();
        final var n = mixArray.length;

        // Array of positions that we will iterate through
        final var positionArray = IntStream.range(0, n).toArray();

        // Lookback array - for each number in the sortarray, what was it's initial position?
        final var initialPositionArray = IntStream.range(0, n).toArray();

        if (context.explain()) {
            printState("Mix array", mixArray);
            printState("Position ", positionArray);
            printState("Initial  ", initialPositionArray);
            System.out.println();
        }

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
