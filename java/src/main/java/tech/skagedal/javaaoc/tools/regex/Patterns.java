package tech.skagedal.javaaoc.tools.regex;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Patterns {

    public static MatchResult match(Pattern pattern, String line) {
        return pattern.matcher(line).results().findFirst().get();
    }
}
