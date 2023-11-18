package tech.skagedal.javaaoc.tools.string;

import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RegexItemParser<T> {
    private final List<ItemFactory<T>> itemFactories;

    @SafeVarargs
    public RegexItemParser(ItemFactory<T>... factories) {
        this(List.of(factories));
    }

    public RegexItemParser(List<ItemFactory<T>> itemFactories) {
        this.itemFactories = itemFactories;
    }

    public T parse(String line) {
        return itemFactories.stream()
            .flatMap(factory -> {
                final var matcher = factory.pattern().matcher(line);
                if (matcher.matches()) {
                    return Stream.of(factory.supplier.apply(matcher.toMatchResult()));
                } else {
                    return Stream.of();
                }
            })
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not parse line: " + line));
    }

    public record ItemFactory<T>(Pattern pattern, Function<MatchResult, T> supplier) { }
}
