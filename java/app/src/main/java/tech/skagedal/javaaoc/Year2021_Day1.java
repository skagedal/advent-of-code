package tech.skagedal.javaaoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.Spliterators;
import tech.skagedal.javaaoc.tools.Streams;

public class Year2021_Day1 {
    public static void main(String[] args) {
        new Year2021_Day1().run();
    }

    private void run() {
        final var numbers = read("day01_input.txt").stream().map(Integer::valueOf).toList();

        // A
        long answerOne = countIncreasing(numbers);
        System.out.println(answerOne);

        // B
        final var lines = readLines("day01_input.txt");
        final var spliterator = lines.spliterator();
        System.out.println(Spliterators.describeSpliterator(spliterator));
    }

    private static long countIncreasing(List<Integer> numbers) {
        return Streams.zip(
                numbers.stream(),
                numbers.stream().skip(1),
                IntPair::new
            )
            .filter(IntPair::isIncreasing)
            .count();
    }



    record IntPair(Integer first, Integer second) {
        boolean isIncreasing() {
            return second > first;
        }
    }

    record IntTriplet(Integer a, Integer b, Integer c) {
        Integer sum() {
            return a + b + c;
        }
    }

    private List<String> read(String filename) {
        final var path = Paths.get("..", "Data", "year2021", filename);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<String> readLines(String filename) {
        final var path = Paths.get("..", "Data", "year2021", filename);
        try (final var reader = Files.newBufferedReader(path)) {
            return reader.lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
