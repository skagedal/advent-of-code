package tech.skagedal.javaaoc.tools.math;

import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.function.Function3;

public class Ints {
    private static final Pattern pattern = Pattern.compile("-?\\d+");

    // Find in string

    public static Stream<Integer> inString(String string) {
        return pattern.matcher(string).results()
            .map(MatchResult::group).map(Integer::parseInt);
    }

    public static <T> T inString(String string, BiFunction<Integer, Integer, T> creator) {
        final var list =  pattern.matcher(string).results()
            .map(MatchResult::group).map(Integer::parseInt).toList();
        return creator.apply(list.get(0), list.get(1));
    }

    public static <T> T inString(String string, Function3<Integer, Integer, Integer, T> creator) {
        final var list =  pattern.matcher(string).results()
            .map(MatchResult::group).map(Integer::parseInt).toList();
        return creator.apply(list.get(0), list.get(1), list.get(2));
    }

}
