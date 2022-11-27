package tech.skagedal.javaaoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.Spliterators;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Year2021_Day1 {
    public static void main(String[] args) {
        new Year2021_Day1().run();
    }

    public long part1() {
        final var numbers = read("day01_input.txt").stream().map(Integer::valueOf).toList();
        return countIncreasing(numbers);
    }

    private void run() {
        // B
        final var lines = readLines("day01_input.txt");
        final var spliterator = lines.spliterator();
        System.out.println(Spliterators.describeSpliterator(spliterator));
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

    private List<String> read(String filename) {
        final var path = findData().resolve("year2021").resolve(filename);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<String> readLines(String filename) {
        final var path = findData().resolve("year2021").resolve(filename);
        try (final var reader = Files.newBufferedReader(path)) {
            return reader.lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path findData() {
        return findData(Paths.get("").toAbsolutePath());
    }

    private static Path findData(Path path) {
        System.out.println("Looking in " + path.toString());
        final var data = path.resolve("Data");
        if (Files.exists(data)) {
            return data;
        }
        final var parent = path.getParent();
        if (parent == null) {
            throw new RuntimeException("Could not find Data dir");
        }
        return findData(parent);
    }

}
