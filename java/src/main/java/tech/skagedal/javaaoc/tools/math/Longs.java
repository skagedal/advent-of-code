package tech.skagedal.javaaoc.tools.math;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Longs {
    private static final Pattern pattern = Pattern.compile("-?\\d+");
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

    // Find in string

    public static Stream<Long> inString(String string) {
        return pattern.matcher(string).results()
            .map(MatchResult::group).map(Long::parseLong);
    }
}
