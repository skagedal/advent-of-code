package tech.skagedal.javaaoc.aoc;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface AdventContext {
    Stream<String> lines();
    IntStream readChars();
}
