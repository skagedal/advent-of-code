package tech.skagedal.javaaoc.tools.streamsetc;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Lists {
    public static <T> List<T> generate(int count, Supplier<T> supplier) {
        return IntStream.range(0, count)
            .mapToObj(ignored -> supplier.get())
            .toList();
    }
}
