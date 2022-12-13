package tech.skagedal.javaaoc.year2022;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Lists;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode
public class Day05 {
    public String part1(AdventContext context) {
        return solvePart(context.lines(), Day05::moveOneByOne);
    }

    public String part2(AdventContext context) {
        return solvePart(context.lines(), Day05::moveAllTogether);
    }

    private String solvePart(Stream<String> lines, Mover mover) {
        final var lists = Streams.splitting(lines, String::isBlank).toList();
        final var initialCargoDescription = lists.get(0);
        final var moves = lists.get(1);

        final var cargo = new Cargo();
        Collections.reverse(initialCargoDescription);
        initialCargoDescription.stream().skip(1).forEach(cargo::addInitialCargoLine);
        moves.forEach(move -> cargo.perform(move, mover));
        return cargo.topLine();
    }

    static class Cargo {
        private final List<Stack<String>> stacks = Lists.generate(9, Stack::new);
        private static final Pattern pattern = Pattern.compile("\\d+");

        void addInitialCargoLine(String line) {
            for (final var enumeratedStack : Streams.toIterable(Streams.enumerated(stacks.stream()))) {
                final var beginIndex = 1 + enumeratedStack.number() * 4;
                final var box = line.substring(beginIndex, beginIndex + 1);
                if (!box.isBlank()) {
                    enumeratedStack.value().push(box);
                }
            }
        }

        public String topLine() {
            StringBuilder total = new StringBuilder();
            for (final var stack : stacks) {
                total.append(stack.peek());
            }
            return total.toString();
        }

        public void perform(String line, Mover mover) {
            final var results = pattern.matcher(line).results().toList();

            final var count = Integer.parseInt(results.get(0).group());
            final var source =  Integer.parseInt(results.get(1).group()) - 1;
            final var destination = Integer.parseInt(results.get(2).group()) - 1;

            mover.move(count, stacks.get(source), stacks.get(destination));
        }
    }

    interface Mover {
        void move(int count, Stack<String> source, Stack<String> destination);
    }

    static void moveOneByOne(int count, Stack<String> source, Stack<String> destination) {
        for (var i = 0; i < count; i++) {
            destination.push(source.pop());
        }
    }

    static void moveAllTogether(int count, Stack<String> source, Stack<String> destination) {
        final var stack = new Stack<String>();
        for (var i = 0; i < count; i++) {
            stack.push(source.pop());
        }
        while(!stack.isEmpty()) {
            destination.push(stack.pop());
        }
    }
}
