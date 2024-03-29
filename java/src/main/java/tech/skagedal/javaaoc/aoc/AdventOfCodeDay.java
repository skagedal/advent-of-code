package tech.skagedal.javaaoc.aoc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public record AdventOfCodeDay(int year, int day, String description, List<AdventOfCodePart> parts, Object object) {
    private static final Pattern dateFromFQCN = Pattern.compile("year([0-9]{4}).*Day([0-9]{2})");

    public static AdventOfCodeDay fromObject(Object object) {
        final var klass = object.getClass();
        final var annotation = klass.getAnnotation(AdventOfCode.class);
        final var match = dateFromFQCN.matcher(klass.getName()).results().findFirst().orElseThrow();
        final var parts = Arrays.stream(klass.getDeclaredMethods())
            .flatMap(method -> AdventOfCodePart.fromObjectAndMethod(object, method).stream())
            .sorted(Comparator.comparing(AdventOfCodePart::number))
            .toList();

        return new AdventOfCodeDay(
            Integer.parseInt(match.group(1)),
            Integer.parseInt(match.group(2)),
            annotation != null ? annotation.description() : "",
            parts,
            object
        );
    }

}

