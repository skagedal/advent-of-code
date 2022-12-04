package tech.skagedal.javaaoc;

import com.google.common.collect.Sets;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.tools.Streams;
import tech.skagedal.javaaoc.tools.Tuple2;

public class Year2022_Day03 extends Year2022Day {
    public long part1() {
        return getLines()
            .map(this::splitInHalf)
            .map(tuple -> Sets.intersection(toSet(tuple.value1()), toSet(tuple.value2())))
            .map(theSet -> theSet.iterator().next())
            .mapToLong(this::getPriority)
            .sum();
    }

    public long part2() {
        var sum = 0;
        final var iterator = getLines().iterator();
        while (iterator.hasNext()) {
            final String a = iterator.next(), b = iterator.next(), c = iterator.next();
            final var intsec = intersection(intersection(a, b), c.chars().boxed().toList()).get(0);
            sum += getPriority(intsec);
        }
        return sum;
    }

    Tuple2<String, String> splitInHalf(String line) {
        return new Tuple2<>(
            line.substring(0, line.length() / 2),
            line.substring(line.length() / 2, line.length())
        );
    }

    Set<Integer> toSet(String string) {
        return string.chars().boxed().collect(Collectors.toSet());
    }

    List<Integer> intersection(String s1, String s2) {
        return intersection(s1.subSequence(0, s1.length()), s2.subSequence(0, s2.length()));
    }

    List<Integer> intersection(CharSequence s1, CharSequence s2) {
        final var s2Set = s2.chars().boxed().collect(Collectors.toSet());
        return s1.chars().boxed().filter(s2Set::contains).toList();
    }

    List<Integer> intersection(List<Integer> l1, List<Integer> l2) {
        final var s2Set = new HashSet<>(l2);
        return l1.stream().filter(s2Set::contains).toList();

    }

    int getPriority(int character) {
        if (character > 'a' && character <= 'z')
            return (character - 'a') + 1;
        if (character > 'A' && character <= 'Z')
            return (character - 'A') + 27;
        throw new IllegalArgumentException("Illegal character: " + character);
    }

    private Stream<String> getLines() {
        return readLines("day03_input.txt");
    }
}
