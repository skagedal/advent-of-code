package tech.skagedal.javaaoc;

import com.google.common.collect.Streams;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Year2021_Day1 {
    public static void main(String[] args) {
        new Year2021_Day1().run();
    }

    private void run() {
        final var lines = read("day01_input.txt");
        System.out.println(Streams.zip(
                lines.stream(),
                lines.stream().skip(1),
                (a, b) -> new IntPair(Integer.valueOf(a), Integer.valueOf(b))
            )
            .filter(IntPair::isIncreasing)
            .count());
    }


    record IntPair(Integer first, Integer second) {
        boolean isIncreasing() {
            return second > first;
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


}
