package tech.skagedal.javaaoc;

import java.util.List;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Year2021_Day1 extends Year2021Day {

    public long part1() {
        final var numbers = readLines("day01_input.txt").map(Integer::valueOf).toList();
        return countIncreasing(numbers);
    }

    public long part2() {
        final var numbers = readLines("day01_input.txt").map(Integer::valueOf).toList();
        final var sumsOfThree = Streams.zip(
            numbers.stream(),
            numbers.stream().skip(1),
            numbers.stream().skip(2),
            (a, b, c) -> a + b + c
        ).toList();
        return countIncreasing(sumsOfThree);
    }

    private static long countIncreasing(List<Integer> numbers) {
        return Streams.zip(
                numbers.stream(),
                numbers.stream().skip(1)
            )
            .filter(Year2021_Day1::isIncreasing)
            .count();
    }

    static private boolean isIncreasing(Tuple2<Integer, Integer> tuple) {
        return tuple.value2() > tuple.value1();
    }


}
