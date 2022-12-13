package tech.skagedal.javaaoc.aoc;

import java.lang.reflect.InvocationTargetException;

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
}
