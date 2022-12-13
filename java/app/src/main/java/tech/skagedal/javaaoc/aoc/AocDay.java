package tech.skagedal.javaaoc.aoc;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AocDay {
    private DataLoaderFactory dataLoaderFactory = new DataLoaderFactory();
    private AdventContext adventContext = dataLoaderFactory.getDataLoader(
        AdventOfCodeDay.fromObject(this)
    );

    protected Stream<String> readLines() {
        return adventContext.lines();
    }

    protected IntStream readChars() {
        return adventContext.readChars();
    }

    protected Stream<String> readExampleLines() {
        return dataLoaderFactory.getExampleDataLoader(AdventOfCodeDay.fromObject(this)).lines();
    }
}
