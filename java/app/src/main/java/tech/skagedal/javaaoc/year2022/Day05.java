package tech.skagedal.javaaoc.year2022;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Pattern;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

public class Day05 extends AocDay {
    public String part1() {
        return solvePart(Day05::moveOneByOne);
    }

    public String part2() {
        return solvePart(Day05::moveAllTogether);
    }

    private String solvePart(Mover mover) {
        final var lists = Streams.splitting(readLines(), String::isBlank).toList();
        final var initialCargoDescription = lists.get(0);
        final var moves = lists.get(1);

        final var cargo = new Cargo();
        Collections.reverse(initialCargoDescription);
        initialCargoDescription.stream().skip(1).forEach(cargo::addInitialCargoLine);
        moves.forEach(move -> cargo.perform(move, mover));
        return cargo.topLine();
    }

    static class Cargo {
        Stack<String>[] stacks = (Stack<String>[])Array.newInstance(Stack.class, 9);
        private static final Pattern pattern = Pattern.compile("\\d+");

        Cargo() {
            for (var i = 0; i < stacks.length; i++) {
                stacks[i] = new Stack<>();
            }
        }

        void addInitialCargoLine(String line) {
            for (var i = 0; i < stacks.length; i++) {
                int beginIndex = 1 + i * 4;
                String box = line.substring(beginIndex, beginIndex + 1);
                if (!box.isBlank()) {
                    stacks[i].push(box);
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

            mover.move(count, stacks[source], stacks[destination]);
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
