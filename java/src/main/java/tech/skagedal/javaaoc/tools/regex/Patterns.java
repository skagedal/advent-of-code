package tech.skagedal.javaaoc.tools.regex;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Patterns {

    public static MatchResult match(Pattern pattern, String line) {
        return pattern.matcher(line).results().findFirst().get();
    }

    public static Optional<MatchResult> find(Pattern pattern, CharSequence text) {
        var matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Optional.of(matcher.toMatchResult());
        } else {
            return Optional.empty();
        }
    }
}
