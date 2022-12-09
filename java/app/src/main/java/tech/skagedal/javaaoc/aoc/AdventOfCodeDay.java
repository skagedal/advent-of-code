package tech.skagedal.javaaoc.aoc;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public record AdventOfCodeDay(int year, int day, List<AdventOfCodePart> parts, Object object) {
    private static final Pattern dateFromFQCN = Pattern.compile("year([0-9]{4}).*Day([0-9]{2})");

    public static AdventOfCodeDay fromObject(Object object) {
        final var klass = object.getClass();
        final var match = dateFromFQCN.matcher(klass.getName()).results().findFirst().orElseThrow();
        final var parts = Arrays.stream(klass.getDeclaredMethods())
            .flatMap(method -> AdventOfCodePart.fromObjectAndMethod(object, method).stream())
            .toList();

        return new AdventOfCodeDay(
            Integer.parseInt(match.group(1)),
            Integer.parseInt(match.group(2)),
            parts,
            object
        );
    }

}

