package tech.skagedal.javaaoc.year2022;

import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;

public class Day25 {
    public String part1(AdventContext context) {
        final var sum = context.lines().mapToLong(Day25::parseSnafu)
            .peek(l -> System.out.printf("Got value: %d\n", l))
            .sum();

        System.out.printf("Sum is %d\n", sum);
        return toSnafu(sum);
    }

    private String toSnafu(long value) {
        var s = "";
        do {
            var rest = (int)(value % 5);
            s = snafuChar(rest) + s;
            value /= 5;
            if (rest > 2) {
                value += 1;
            }
        } while (value > 0);
        return s;
    }

    public static long parseSnafu(String s) {
        long result = 0;
        long multipler = 1;
        for (var exp = 0; exp < s.length(); exp++) {
            final var digit = snafuValue(s.charAt(s.length() - exp - 1));
            result = result + digit * multipler;
            multipler = multipler * 5;
        }
        return result;
    }

    private static long snafuValue(int c) {
        return switch (c) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '-' -> -1;
            case '=' -> -2;
            default -> throw new IllegalArgumentException("illegal snafu character: " + c);
        };
    }

    private static String snafuChar(int val) {
        return switch (val) {
            case 0 -> "0";
            case 1 -> "1";
            case 2 -> "2";
            case 3 -> "=";
            case 4 -> "-";
            default -> throw new IllegalArgumentException("illegal value: " + val);
        };
    }

    public static void main(String[] args) {
        var day = new Day25();

        AdventOfCodeRunner.example(day, """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122""");
        AdventOfCodeRunner.run(day);
    }
}
