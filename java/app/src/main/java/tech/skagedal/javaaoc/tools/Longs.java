package tech.skagedal.javaaoc.tools;

import java.util.Optional;

public class Longs {
    public static Optional<Long> parseOptional(String string) {
        try {
            return Optional.of(Long.parseLong(string));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
