package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode
public class Day11 {
    public long part1(AdventContext context) {
        Game game = new Game(context.lines(), 20);
        return game.run(worry -> worry / 3);
    }

    // TODO: Disabled as it takes a long time to calculate
    public long part2Disabled(AdventContext context) {
        Game game = new Game(context.lines(), 10000);
        final var worryModulo = game.monkeys.stream().mapToLong(Monkey::getDivisor).reduce(1, (a, b) -> a * b);
        return game.run(worry -> worry % worryModulo);
    }

    interface WorryManager {
        long managedWorry(long worry);
    }

    private class Game {
        private final List<Monkey> monkeys;
        private final int rounds;


        private Game(Stream<String> lines, int rounds) {
            this.monkeys = readMonkeys(lines).toList();
            this.rounds = rounds;
        }

        long run(WorryManager worryManager) {
            for (var i = 0; i < rounds; i++) {
                round(worryManager);
                printRoundInfo(i + 1);
            }

            System.out.println(monkeys);

            return monkeys.stream().sorted(Comparator.comparing(Monkey::getInspectedItems).reversed())
                .limit(2).mapToLong(Monkey::getInspectedItems).reduce(1, (a, b) -> a * b);
        }

        private void printRoundInfo(int round) {
            System.out.printf("== After round %d ==\n", round);
            System.out.println("Monkeys are holding:");
            for (var monkey : monkeys) {
                System.out.printf("Monkey %d: %s\n", monkey.num, monkey.items);
            }
            System.out.println();
            for (var monkey : monkeys) {
                System.out.printf("Monkey %d inspected items %d times.\n", monkey.num, monkey.inspectedItems);
            }
            System.out.println();
        }

        void round(WorryManager worryManager) {
            for (var monkey : monkeys) {
                System.out.printf("Monkey %d:\n", monkey.num);
                for (var currentLevel : monkey.items) {
                    System.out.printf("  Monkey inspects an item with worry level of %d\n", currentLevel);
                    monkey.inspectedItems++;
                    final long operand = switch (monkey.operand) {
                        case Operand.Old old -> currentLevel;
                        case Operand.Val val -> val.value;
                    };
                    final var oppedWorry = switch (monkey.op) {
                        case PLUS -> currentLevel + operand;
                        case TIMES -> currentLevel * operand;
                    };
                    System.out.printf("    It is %s with %s (%d) to %d.\n", monkey.op, monkey.operand, operand, oppedWorry);
                    final var finalWorry = worryManager.managedWorry(oppedWorry);
                    // final var finalWorry = oppedWorry / 3;
                    System.out.printf("    Monkey gets bored with item. Worry level is managed to be %d.\n", finalWorry);
                    long newMonkey;
                    if (finalWorry % monkey.divisor == 0) {
                        System.out.printf("    Current worry level is divisible by %d.\n", monkey.divisor);
                        newMonkey = monkey.ifTrueMonkey;
                    } else {
                        System.out.printf("    Current worry level is not divisible by %d.\n", monkey.divisor);
                        newMonkey = monkey.ifFalseMonkey;
                    }
                    System.out.printf("    Item with worry level %d is thrown to monkey %d.\n", finalWorry, newMonkey);
                    monkeys.get((int)newMonkey).items.add(finalWorry);
                }
                monkey.items.clear(); // Has thrown all its items
            }
        }
    }

    private Stream<Monkey> readMonkeys(Stream<String> lines) {
        return Streams.splitting(lines, String::isBlank).map(Monkey::parse);
    }

    private static class Monkey {
        final long num;
        final List<Long> items;
        final Op op;
        final Operand operand;
        final long divisor;
        final long ifTrueMonkey;
        final long ifFalseMonkey;
        long inspectedItems = 0;

        public Monkey(long num, List<Long> items, Op op, Operand operand, long divisor, long ifTrueMonkey, long ifFalseMonkey) {
            this.num = num;
            this.items = new ArrayList<>(items);
            this.op = op;
            this.operand = operand;
            this.divisor = divisor;
            this.ifTrueMonkey = ifTrueMonkey;
            this.ifFalseMonkey = ifFalseMonkey;
        }

        public long getInspectedItems() {
            return inspectedItems;
        }

        public long getDivisor() {
            return divisor;
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

        private static long number(String string) {
            return numbers(string).findFirst().orElseThrow();
        }

        private static Stream<Long> numbers(String string) {
            return NUMBER.matcher(string).results().map(MatchResult::group).map(Long::parseLong);
        }

        @Override
        public String toString() {
            return "Monkey{" +
                   "num=" + num +
                   ", items=" + items +
                   ", op=" + op +
                   ", operand=" + operand +
                   ", divisibleBy=" + divisor +
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
        record Val(long value) implements Operand {}
    }
}
