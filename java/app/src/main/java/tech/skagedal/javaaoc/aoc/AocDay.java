package tech.skagedal.javaaoc.aoc;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AocDay {
    private DataLoaderFactory dataLoaderFactory = new DataLoaderFactory();
    private DataLoader dataLoader = dataLoaderFactory.getDataLoader(
        AdventOfCodeDay.fromObject(this)
    );

    protected Stream<String> readLines() {
        return dataLoader.readLines();
    }

    protected IntStream readChars() {
        return dataLoader.readChars();
    }
}
