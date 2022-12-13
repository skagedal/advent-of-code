package tech.skagedal.javaaoc.tools.streamsetc;

import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpliteratorUtil {

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

    public static long composedEstimateSize(Spliterator<?>... spliterators) {
        return Stream.of(spliterators)
            .map(Spliterator::estimateSize)
            .min(Comparator.comparingLong(val -> val))
            .orElseThrow();
    }

    public static int composedCharacteristics(Spliterator<?>... spliterators) {
        return Stream.of(spliterators)
            .map(Spliterator::characteristics)
            .reduce(
                // Characteristics we inherit from composed spliterators if all have them
                Spliterator.SIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL,
                (c1, c2) -> c1 & c2
                )
            // Characteristics that we always set
            | Spliterator.ORDERED;
    }
}
