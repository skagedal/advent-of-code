package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Sets;
import java.util.Set;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.string.Strings;
import tech.skagedal.javaaoc.tools.function.Tuple2;

@AdventOfCode
public class Day03 {
    public long part1(AdventContext context) {
        return context.lines()
            .map(Day03::splitStringInHalf)
            .map(tuple -> Sets.intersection(Strings.toSet(tuple.value1()), Strings.toSet(tuple.value2())))
            .map(Day03::singleElementOfSet)
            .mapToLong(this::getPriority)
            .sum();
    }

    public long part2(AdventContext context) {
        return Streams.splittingFixedSize(context.lines().map(Strings::toSet), 3)
            .map(list -> list.stream().reduce(Sets::intersection).orElseThrow())
            .map(Day03::singleElementOfSet)
            .mapToLong(this::getPriority)
            .sum();
    }

    int getPriority(int character) {
        if (character > 'a' && character <= 'z')
            return (character - 'a') + 1;
        if (character > 'A' && character <= 'Z')
            return (character - 'A') + 27;
        throw new IllegalArgumentException("Illegal character: " + character);
    }

    static Tuple2<String, String> splitStringInHalf(String line) {
        return new Tuple2<>(
            line.substring(0, line.length() / 2),
            line.substring(line.length() / 2)
        );
    }

    static <T> T singleElementOfSet(Set<T> set) {
        final var iterator = set.iterator();
        final var element = iterator.next();
        if (iterator.hasNext()) {
            throw new IllegalStateException("Expected set to have just one element");
        }
        return element;
    }
}
