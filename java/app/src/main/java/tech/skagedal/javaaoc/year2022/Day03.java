package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.Year2022Day;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Day03 extends Year2022Day {
    public long part1() {
        return readLines()
            .map(Util::splitStringInHalf)
            .map(tuple -> Sets.intersection(toSet(tuple.value1()), toSet(tuple.value2())))
            .map(Util::singleElementOfSet)
            .mapToLong(this::getPriority)
            .sum();
    }

    public long part2() {
        return Streams.splittingFixedSize(readLines().map(this::toSet), 3)
            .map(list -> list.stream().reduce(Sets::intersection).get())
            .map(Util::singleElementOfSet)
            .mapToLong(this::getPriority)
            .sum();
    }

    Set<Integer> toSet(String string) {
        return string.chars().boxed().collect(Collectors.toSet());
    }

    int getPriority(int character) {
        if (character > 'a' && character <= 'z')
            return (character - 'a') + 1;
        if (character > 'A' && character <= 'Z')
            return (character - 'A') + 27;
        throw new IllegalArgumentException("Illegal character: " + character);
    }
}

class Util {
    static Tuple2<String, String> splitStringInHalf(String line) {
        return new Tuple2<>(
            line.substring(0, line.length() / 2),
            line.substring(line.length() / 2, line.length())
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

