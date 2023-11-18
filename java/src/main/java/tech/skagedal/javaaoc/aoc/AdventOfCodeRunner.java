package tech.skagedal.javaaoc.aoc;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import tech.skagedal.javaaoc.year2022.Day15;

public class AdventOfCodeRunner {
    private static DataLoaderFactory factory = new DataLoaderFactory();
    public static void example(Object object) {
        final var day = AdventOfCodeDay.fromObject(object);
        for (final var part : day.parts()) {
            try {
                AdventContext context = factory.getExampleDataContext(day);
                System.out.println(part.method().invoke(object, context));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void example(Object object, String data) {
        final var day = AdventOfCodeDay.fromObject(object);
        for (final var part : day.parts()) {
            try {
                AdventContext context = AdventContext.fromString(data);
                System.out.println(part.method().invoke(object, context));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void run(Object object) {
        final var day = AdventOfCodeDay.fromObject(object);
        for (final var part : day.parts()) {
            try {
                AdventContext context = factory.getDataLoader(day);
                final var before = System.nanoTime();
                final var result = part.method().invoke(object, context).toString();
                final var duration = Duration.ofNanos(System.nanoTime() - before);
                System.out.printf("%s-%02d part %d: %s - took %s\n", day.year(), day.day(), part.number(), result, duration);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
