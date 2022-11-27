package tech.skagedal.javaaoc.tools;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;

public class Spliterators {

    private record SpliteratorCharacteristic(int characteristic, String name) {}

    private static List<SpliteratorCharacteristic> descriptions = List.of(
        new SpliteratorCharacteristic(Spliterator.ORDERED, "ORDERED"),
        new SpliteratorCharacteristic(Spliterator.DISTINCT, "DISTINCT"),
        new SpliteratorCharacteristic(Spliterator.SORTED, "SORTED"),
        new SpliteratorCharacteristic(Spliterator.SIZED, "SIZED"),
        new SpliteratorCharacteristic(Spliterator.NONNULL, "NONNULL"),
        new SpliteratorCharacteristic(Spliterator.IMMUTABLE, "IMMUTABLE"),
        new SpliteratorCharacteristic(Spliterator.CONCURRENT, "IMMUTABLE"),
        new SpliteratorCharacteristic(Spliterator.SUBSIZED, "SUBSIZED")
    );

    public static String describeSpliterator(Spliterator<?> spliterator) {
        final var characteristics = spliterator.characteristics();

        return descriptions.stream()
            .filter(description -> (characteristics & description.characteristic()) == description.characteristic())
            .map(SpliteratorCharacteristic::name)
            .collect(Collectors.joining(", "));
    }
}
