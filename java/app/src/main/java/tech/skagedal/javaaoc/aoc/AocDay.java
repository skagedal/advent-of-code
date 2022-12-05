package tech.skagedal.javaaoc.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AocDay {
    private static final Pattern fileNameFromFQCN = Pattern.compile("\\.([a-z0-9]+)\\.([a-zA-Z0-9]+)$");

    protected static Path findData() {
        return AocDay.findData(Paths.get("").toAbsolutePath());
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
        return AocDay.findData(parent);
    }

    protected Stream<String> readLines() {
        final var match = fileNameFromFQCN.matcher(this.getClass().getName())
            .results().findFirst().orElseThrow();
        return readLines(
            match.group(1).toLowerCase(),
            match.group(2).toLowerCase() + "_input.txt"
        );
    }

    private Stream<String> readLines(String yearDirectory, String filename) {
        final var path = findData().resolve(yearDirectory).resolve(filename);
        try {
            return Files.newBufferedReader(path).lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
