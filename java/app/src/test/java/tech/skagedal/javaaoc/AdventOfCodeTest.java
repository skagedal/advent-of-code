package tech.skagedal.javaaoc;

import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeDay;
import tech.skagedal.javaaoc.aoc.AdventOfCodePart;
import tech.skagedal.javaaoc.aoc.DataLoaderFactory;

public class AdventOfCodeTest {
    private DataLoaderFactory dataLoaderFactory = new DataLoaderFactory();

    @TestFactory
    Stream<DynamicNode> adventOfCodeTests() {
        final var context = Main.createApplicationContext();

        return context
            .getBeanFactory()
            .getBeansWithAnnotation(AdventOfCode.class)
            .values().stream().map(this::createNode);
    }

    private DynamicNode createNode(Object object) {
        final var day = AdventOfCodeDay.fromObject(object);
        final var answers = dataLoaderFactory.loadAnswers(day).toList();

        return DynamicContainer.dynamicContainer(
            String.format("%04d-%02d", day.year(), day.day()),
            day.parts().stream()
                .sorted(Comparator.comparing(AdventOfCodePart::number))
                .map(part -> DynamicTest.dynamicTest(
                    String.format("Part %d", part.number()),
                    () -> {
                        final var answer = part.method().invoke(object).toString();
                        if (part.number() - 1 < answers.size()) {
                            final var expectedAnswer = answers.get(part.number() - 1);
                            Assertions.assertEquals(
                                expectedAnswer,
                                answer
                            );
                        } else {
                            Assertions.fail("NEW ANSWER: " + answer);
                        }
                    }
                ))
        );
    }

}
