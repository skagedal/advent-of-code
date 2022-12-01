package tech.skagedal.javaaoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class AocDay {
    private final String yearDirectoryName;

    public AocDay(String yearDirectoryName) {
        this.yearDirectoryName = yearDirectoryName;
    }

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

    protected Stream<String> readLines(String filename) {
        final var path = findData().resolve(yearDirectoryName).resolve(filename);
        try {
            return Files.newBufferedReader(path).lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
