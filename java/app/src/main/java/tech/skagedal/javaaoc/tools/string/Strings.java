package tech.skagedal.javaaoc.tools.string;

import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Strings {
    public static Set<Integer> toSet(String string) {
        return string.chars().boxed().collect(Collectors.toSet());
    }

    public static Stream<String> inString(String string, Pattern pattern) {
        return pattern.matcher(string).results().map(MatchResult::group);
    }

}
