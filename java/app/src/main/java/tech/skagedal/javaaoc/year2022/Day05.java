package tech.skagedal.javaaoc.year2022;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.Year2022Day;
import tech.skagedal.javaaoc.tools.Streams;

public class Day05 extends Year2022Day {
    public String part1() {
        final var lists = Streams.splitting(getLines(), String::isBlank).toList();
        final var initialCargo = lists.get(0);
        final var moves = lists.get(1);

        var cargo = new Cargo();
        Collections.reverse(initialCargo);
        initialCargo.stream().skip(1).forEach(cargo::addInitialCargoLine);
        moves.stream().forEach(cargo::addMove);
        return cargo.topLine();
    }

    public String part2() {
        final var lists = Streams.splitting(getLines(), String::isBlank).toList();
        final var initialCargo = lists.get(0);
        final var moves = lists.get(1);

        var cargo = new Cargo();
        Collections.reverse(initialCargo);
        initialCargo.stream().skip(1).forEach(cargo::addInitialCargoLine);
        moves.stream().forEach(cargo::addMultiMove);
        return cargo.topLine();
    }

    class Cargo {
        Stack<String>[] stacks = (Stack<String>[])Array.newInstance(Stack.class, 9);
        private static Pattern pattern = Pattern.compile("\\d+");

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

        public void addMove(String line) {
            final var results = pattern.matcher(line).results().toList();

            final var count = Integer.parseInt(results.get(0).group());
            final var source =  Integer.parseInt(results.get(1).group()) - 1;
            final var destination = Integer.parseInt(results.get(2).group()) - 1;

            for (var i = 0; i < count; i++) {
                stacks[destination].push(stacks[source].pop());
            }
        }

        public String topLine() {
            var total = "";
            for (var i = 0; i < stacks.length; i++) {
                total += stacks[i].peek();
            }
            return total;
        }

        public void addMultiMove(String line) {
            final var results = pattern.matcher(line).results().toList();

            final var count = Integer.parseInt(results.get(0).group());
            final var source =  Integer.parseInt(results.get(1).group()) - 1;
            final var destination = Integer.parseInt(results.get(2).group()) - 1;

            final var stack = new Stack<String>();
            for (var i = 0; i < count; i++) {
                stack.push(stacks[source].pop());
            }
            while(!stack.isEmpty()) {
                stacks[destination].push(stack.pop());
            }
        }
    }

    private Stream<String> getLines() {
        return readLines("day05_input.txt");
    }
}
