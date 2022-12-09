package tech.skagedal.javaaoc.aoc;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.regex.Pattern;

public record AdventOfCodePart(int number, Object object, Method method) {
    private static final Pattern partFromMethod = Pattern.compile("^part([1-9]+)$");

    public static Optional<AdventOfCodePart> fromObjectAndMethod(Object object, Method method) {
        return partFromMethod.matcher(method.getName())
            .results().findFirst()
            .map(match -> new AdventOfCodePart(Integer.parseInt(match.group(1)), object, method));
    }
}
