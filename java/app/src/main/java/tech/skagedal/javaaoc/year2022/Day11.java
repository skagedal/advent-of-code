package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AocDay;
import tech.skagedal.javaaoc.tools.Streams;

@AdventOfCode
public class Day11 extends AocDay {
    public  long part1() {
        var game = new Game();
        return game.run();
    }

    private class Game {
        List<Monkey> monkeys = readMonkeys().toList();

        long run() {
            for (var i = 0; i < 20; i++) {
                round();
                roundInfo(i + 1);
            }

            System.out.println(monkeys);

            return monkeys.stream().sorted(Comparator.comparing(Monkey::getInspectedItems).reversed())
                .limit(2).mapToLong(Monkey::getInspectedItems).reduce(1, (a, b) -> a * b);
        }

        private void roundInfo(int round) {
            System.out.printf("\nAfter round %d, the monkeys are holding:\n", round);
            for (var monkey : monkeys) {
                System.out.printf("Monkey %d: %s\n", monkey.num, monkey.items);
            }
            System.out.println();
        }

        void round() {
            for (var monkey : monkeys) {
                System.out.printf("Monkey %d:\n", monkey.num);
                for (var currentLevel : monkey.items) {
                    System.out.printf("  Monkey inspects an item with worry level of %d\n", currentLevel);
                    monkey.inspectedItems++;
                    final int operand = switch (monkey.operand) {
                        case Operand.Old old -> currentLevel;
                        case Operand.Val val -> val.value;
                    };
                    final var oppedWorry = switch (monkey.op) {
                        case PLUS -> currentLevel + operand;
                        case TIMES -> currentLevel * operand;
                    };
                    System.out.printf("    It is %s with %s (%d) to %d.\n", monkey.op, monkey.operand, operand, oppedWorry);
                    final var finalWorry = oppedWorry / 3;
                    System.out.printf("    Monkey gets bored with item. Worry level is diveded by 3 to %d.\n", finalWorry);
                    int newMonkey;
                    if (finalWorry % monkey.divisibleBy == 0) {
                        System.out.printf("    Current worry level is divisible by %d.\n", monkey.divisibleBy);
                        newMonkey = monkey.ifTrueMonkey;
                    } else {
                        System.out.printf("    Current worry level is not divisible by %d.\n", monkey.divisibleBy);
                        newMonkey = monkey.ifFalseMonkey;
                    }
                    System.out.printf("    Item with worry level %d is thrown to monkey %d.\n", finalWorry, newMonkey);
                    monkeys.get(newMonkey).items.add(finalWorry);
                }
                monkey.items.clear(); // Has thrown all its items
            }
        }
    }

    private Stream<Monkey> readMonkeys() {
        return Streams.splitting(readLines(), String::isBlank).map(Monkey::parse);
    }

    private static class Monkey {
        final int num;
        final List<Integer> items;
        final Op op;
        final Operand operand;
        final int divisibleBy;
        final int ifTrueMonkey;
        final int ifFalseMonkey;
        int inspectedItems = 0;

        public Monkey(int num, List<Integer> items, Op op, Operand operand, int divisibleBy, int ifTrueMonkey, int ifFalseMonkey) {
            this.num = num;
            this.items = new ArrayList<>(items);
            this.op = op;
            this.operand = operand;
            this.divisibleBy = divisibleBy;
            this.ifTrueMonkey = ifTrueMonkey;
            this.ifFalseMonkey = ifFalseMonkey;
        }

        public int getInspectedItems() {
            return inspectedItems;
        }

        static Pattern NUMBER = Pattern.compile("\\d+");
        static Pattern OP = Pattern.compile("[+*]");


        static Monkey parse(List<String> lines) {
            final var iter = lines.iterator();

            final var monkeyNumber = number(iter.next());
            final var items = numbers(iter.next()).toList();
            final var opLine = iter.next();
            final var op = OP.matcher(opLine).results().map(MatchResult::group).map(Op::fromString).findFirst().orElseThrow();
            final var operand = numbers(opLine).findFirst().<Operand>map(Operand.Val::new).orElse(new Operand.Old());
            final var divisibleBy = number(iter.next());
            final var ifTrueMonkey = number(iter.next());
            final var ifFalseMonkey = number(iter.next());
            return new Monkey(monkeyNumber, items, op, operand, divisibleBy, ifTrueMonkey, ifFalseMonkey);
        }

        private static int number(String string) {
            return numbers(string).findFirst().orElseThrow();
        }

        private static Stream<Integer> numbers(String string) {
            return NUMBER.matcher(string).results().map(MatchResult::group).map(Integer::parseInt);
        }

        @Override
        public String toString() {
            return "Monkey{" +
                   "num=" + num +
                   ", items=" + items +
                   ", op=" + op +
                   ", operand=" + operand +
                   ", divisibleBy=" + divisibleBy +
                   ", ifTrueMonkey=" + ifTrueMonkey +
                   ", ifFalseMonkey=" + ifFalseMonkey +
                   ", inspectedItems=" + inspectedItems +
                   '}';
        }
    }

    private enum Op {
        PLUS, TIMES;

        static Op fromString(String string) {
            return switch (string) {
                case "*" -> TIMES;
                case "+" -> PLUS;
                default -> throw new IllegalArgumentException("unknown operator: " + string);
            };
        }
    }

    sealed interface Operand {
        record Old() implements Operand {}
        record Val(int value) implements Operand {}
    }

    public static void main(String[] args) {
        System.out.println(new Day11().part1());
    }
}
