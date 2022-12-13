package tech.skagedal.javaaoc.tools.math;

import java.util.Optional;

public class Longs {
    public static long multiply(long a, long b) {
        return a * b;
    }

    public static Optional<Long> parseOptional(String string) {
        try {
            return Optional.of(Long.parseLong(string));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
