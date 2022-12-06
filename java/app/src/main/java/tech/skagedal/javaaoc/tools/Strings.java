package tech.skagedal.javaaoc.tools;

import java.util.Set;
import java.util.stream.Collectors;

public class Strings {
    public static Set<Integer> toSet(String string) {
        return string.chars().boxed().collect(Collectors.toSet());
    }
}
