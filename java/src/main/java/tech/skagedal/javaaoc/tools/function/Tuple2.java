package tech.skagedal.javaaoc.tools.function;

import java.util.function.BiFunction;
import java.util.function.Function;

public record Tuple2<T1, T2>(T1 value1, T2 value2) {
    // Not fully sure this is what's called "splat" but it can be useful anyway
    public static <T1, T2, R> Function<Tuple2<T1, T2>, R> splat(BiFunction<T1, T2, R> biFunction) {
        return tuple -> biFunction.apply(tuple.value1(), tuple.value2());
    }
}
