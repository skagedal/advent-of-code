package tech.skagedal.javaaoc.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AocDay {
    private static final Pattern fileNameFromFQCN = Pattern.compile("\\.([a-z0-9]+)\\.([a-zA-Z0-9]+)$");

    protected static Path findData() {
        return AocDay.findData(Paths.get("").toAbsolutePath());
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
        return AocDay.findData(parent);
    }

    protected Stream<String> readLines() {
        return readLines(getPath());
    }

    protected IntStream readChars() {
        return readChars(getPath());
    }

    private Path getPath() {
        final var match = fileNameFromFQCN.matcher(this.getClass().getName())
            .results().findFirst().orElseThrow();
        final var path = findData().resolve(match.group(1).toLowerCase()).resolve(
            match.group(2).toLowerCase() + "_input.txt");
        return path;
    }

    private static Stream<String> readLines(Path path) {
        try {
            return Files.newBufferedReader(path).lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private IntStream readChars(Path path) {
        try {
            BufferedReader reader = Files.newBufferedReader(path);
            return IntStream
                .generate(checkSupplier(reader::read))
                .onClose(checkRunnable(reader::close));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    interface SneakyIntSupplier {
        int supply() throws Exception;
    }

    interface SneakyRunnable {
        void run() throws Exception;
    }

    private IntSupplier checkSupplier(SneakyIntSupplier supplier) {
        return () -> {
            try {
                return supplier.supply();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    private Runnable checkRunnable(SneakyRunnable runnable) {
        return () -> {
            try {
                runnable.run();;
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }
}
