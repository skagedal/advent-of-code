package tech.skagedal.javaaoc.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.sneaky.Checked;

public class DataLoaderFactory {
    private Path dataPath = findData();

    public DataLoader getDataLoader(AdventOfCodeDay day) {
        final var path = inputPath(day);
        return new DataLoader() {
            @Override
            public Stream<String> readLines() {
                try {
                    return Files.newBufferedReader(path).lines();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public IntStream readChars() {
                try {
                    BufferedReader reader = Files.newBufferedReader(path);
                    return IntStream
                        .generate(Checked.intSupplier(reader::read))
                        .onClose(Checked.runnable(reader::close));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public Stream<String> loadAnswers(AdventOfCodeDay day) {
        try {
            return Files.newBufferedReader(answersPath(day)).lines().filter(Predicate.not(String::isBlank));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Path inputPath(AdventOfCodeDay day) {
        return yearPath(day)
            .resolve(String.format("day%02d_input.txt", day.day()));
    }

    private Path answersPath(AdventOfCodeDay day) {
        return yearPath(day)
            .resolve(String.format("day%02d_answers.txt", day.day()));
    }

    private Path yearPath(AdventOfCodeDay day) {
        return dataPath
            .resolve(String.format("year%04d", day.year()));
    }


    private static Path findData() {
        return DataLoaderFactory.findData(Paths.get("").toAbsolutePath());
    }

    private static Path findData(Path path) {
        final var data = path.resolve("Data");
        if (Files.exists(data)) {
            return data;
        }
        final var parent = path.getParent();
        if (parent == null) {
            throw new RuntimeException("Could not find Data dir");
        }
        return DataLoaderFactory.findData(parent);
    }

}
