package tech.skagedal.javaaoc.aoc;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface AdventContext {
    static AdventContext fromString(String str) {
        return new AdventContext() {
            @Override
            public Stream<String> lines() {
                return str.lines();
            }

            @Override
            public IntStream chars() {
                return str.chars();
            }

            @Override
            public boolean explain() {
                return true;
            }
        };
    }

    Stream<String> lines();
    IntStream chars();

    default boolean explain() {
        return false;
    }
}
