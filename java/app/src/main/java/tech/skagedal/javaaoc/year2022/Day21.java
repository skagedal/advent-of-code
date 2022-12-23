package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.Action;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.string.RegexItemParser;

@AdventOfCode(
    description = "Monkey Math"
)
public class Day21 {
    public long part1(AdventContext context) {
        final var monkeys = context.lines().map(MonkeyDescription::fromString).map(Monkey::new).collect(Collectors.toMap(
            monkey -> monkey.description.name(),
            Function.identity()
        ));

        return findValue(monkeys, "root");
    }

    public long part2(AdventContext context) {
        final var monkeys = context.lines().map(MonkeyDescription::fromString).map(Monkey::new).collect(Collectors.toMap(
            monkey -> monkey.description.name(),
            Function.identity()
        ));

        var eq = buildEquation(monkeys);
        Equation next = eq;
        do {
            eq = next;
            next = eq.simplify();
        } while (!eq.equals(next));

        if (eq.left() instanceof Expr.Unknown && eq.right instanceof Expr.Const (var c)) {
            return c;
        } else {
            System.out.println(eq);
            throw new IllegalStateException("Could not solve equation");
        }
    }

    private long findValue(Map<String, Monkey> monkeys, String name) {
        final var monkey = monkeys.get(name);
        if (monkey == null) {
            throw new IllegalArgumentException("No such monkey");
        }
        if (monkey.value != null) {
            return monkey.value;
        }
        final long value = switch (monkey.description) {
            case MonkeyDescription.Operation op -> op.op.apply(
                findValue(monkeys, op.a()),
                findValue(monkeys, op.b())
            );
            case MonkeyDescription.Number num -> num.number();
        };
        monkey.value = value;
        return value;
    }

    private Equation buildEquation(Map<String, Monkey> monkeys) {
        final var root = monkeys.get("root");
        if (root.description instanceof MonkeyDescription.Operation operation) {
            return new Equation(
                buildExpr(monkeys, operation.a()),
                buildExpr(monkeys, operation.b())
            );
        } else {
            throw new IllegalArgumentException("Unexpected root monkey");
        }
    }

    private Expr buildExpr(Map<String, Monkey> monkeys, String name) {
        if (name.equals("humn")) {
            return new Expr.Unknown();
        }
        final var monkey = monkeys.get(name);
        if (monkey == null) {
            throw new IllegalArgumentException("No such monkey: " + name);
        }
        return switch (monkey.description) {
            case MonkeyDescription.Number number -> new Expr.Const(number.number());
            case MonkeyDescription.Operation operation -> {
                final var a = buildExpr(monkeys, operation.a());
                final var b = buildExpr(monkeys, operation.b());
                yield switch (operation.op()) {
                    case PLUS -> new Expr.Plus(a, b);
                    case MINUS -> new Expr.Minus(a, b);
                    case TIMES -> new Expr.Times(a, b);
                    case DIVIDED -> new Expr.Div(a, b);
                };
            }
        };
    }

    static class Monkey {
        final MonkeyDescription description;
        Long value;
        List<Monkey> subscribers = new ArrayList<>();

        Monkey(MonkeyDescription description) {
            this.description = description;
        }
    }

    enum Op {
        PLUS, MINUS, TIMES, DIVIDED;

        static Op fromString(String s) {
            return switch (s) {
                case "+" -> PLUS;
                case "-" -> MINUS;
                case "*" -> TIMES;
                case "/" -> DIVIDED;
                default -> throw new IllegalArgumentException("unknown operator: " + s);
            };
        }

        public long apply(long a, long b) {
            return switch (this) {
                case PLUS -> a + b;
                case MINUS -> a - b;
                case TIMES -> a * b;
                case DIVIDED -> a / b;
            };
        }
    }
    sealed interface MonkeyDescription {
        String name();
        record Number(String name, long number) implements MonkeyDescription {}
        record Operation(String name, Op op, String a, String b) implements MonkeyDescription {}

        Pattern NUM_PATTERN = Pattern.compile("([a-z]{4}): ([0-9]+)");
        Pattern OP_PATTERN = Pattern.compile("([a-z]{4}): ([a-z]{4}) ([-+*/]) ([a-z]{4})");

        RegexItemParser<MonkeyDescription> parser = new RegexItemParser<>(
            new RegexItemParser.ItemFactory<>(OP_PATTERN, result ->
                new Operation(
                    result.group(1),
                    Op.fromString(result.group(3)),
                    result.group(2),
                    result.group(4)
                )),
            new RegexItemParser.ItemFactory<>(NUM_PATTERN, result ->
                new Number(result.group(1), Long.parseLong(result.group(2))))
        );

        static MonkeyDescription fromString(String s) {
            return parser.parse(s);
        }
    }

    record Equation(Expr left, Expr right) {
        @Override
        public String toString() {
            return String.format("%s = %s", left, right);
        }

        public Equation simplify() {
            Equation simplified = simplifiedOperands();
            return switch (simplified) {
                // a1 + a2 = b
                case Equation(Expr.Plus (Expr a1, Expr.Const a2), Expr.Const b) ->
                    new Equation(a1, new Expr.Minus(b, a2));
                case Equation(Expr.Plus (Expr a1, Expr a2), Expr.Const b) ->
                    new Equation(a2, new Expr.Minus(b, a1));
                // a1 - a2 = b
                case Equation(Expr.Minus (Expr a1, Expr.Const a2), Expr.Const b) ->
                    new Equation(a1, new Expr.Plus(b, a2));
                // a1 - a2 = b ==> -a2 = b - a1 ==> a2 = a1 - b
                case Equation(Expr.Minus (Expr a1, Expr a2), Expr.Const b) ->
                    new Equation(a2, new Expr.Minus(a1, b));
                // a1 * a2 = b ==> a1 = b / a2
                case Equation(Expr.Times (Expr a1, Expr.Const a2), Expr.Const b) ->
                    new Equation(a1, new Expr.Div(b, a2));
                case Equation(Expr.Times (Expr a1, Expr a2), Expr.Const b) ->
                    new Equation(a2, new Expr.Div(b, a1));
                // a1 / a2 = b ==> a1 = b * a2
                case Equation(Expr.Div (Expr a1, Expr.Const a2), Expr.Const b) ->
                    new Equation(a1, new Expr.Times(a2, b));
                // a1 / a2 = b ==> 1 / a2 = b / a1 ==> a2 = a1 / b
                case Equation(Expr.Div (Expr a1, Expr a2), Expr.Const b) ->
                    new Equation(a2, new Expr.Div(a1, b));
                default -> simplified;
            };
        }

        private Equation simplifiedOperands() {
            return switch (new Equation(left.simplify(), right.simplify())) {
                case Equation(Expr.Const a_, Expr.Const b_) -> throw new IllegalStateException("That's not an equation");
                case Equation(Expr a_, Expr.Const b_) -> new Equation(a_, b_);
                case Equation(Expr.Const a_, Expr b_) -> new Equation(b_, a_);
                case Equation(Expr a_, Expr b_) -> throw new IllegalStateException("We don't support unknowns on both sides");
            };
        }

    }

    sealed interface Expr {
        default Expr simplify() {
            return switch (this.simplifiedOperands()) {
                case Plus(Const (var a), Const (var b)) -> new Const(a + b);
                case Minus(Const (var a), Const (var b)) -> new Const(a - b);
                case Times(Const (var a), Const (var b)) -> new Const(a * b);
                case Div(Const (var a), Const (var b)) -> {
                    if (a % b != 0) {
                        System.out.println("WARNING: Lossy division");
                    }
                    yield new Const(a / b);
                }
                default -> this;
            };
        }

        default Expr simplifiedOperands() {
            return switch (this) {
                case Plus(var a, var b) -> new Plus(a.simplify(), b.simplify());
                case Minus(var a, var b) -> new Minus(a.simplify(), b.simplify());
                case Times(var a, var b) -> new Times(a.simplify(), b.simplify());
                case Div(var a, var b) -> new Div(a.simplify(), b.simplify());
                default -> this;
            };
        }
        record Unknown() implements Expr {
            @Override
            public String toString() {
                return "x";
            }
        }
        record Const(long num) implements Expr {
            @Override
            public String toString() {
                return "" + num;
            }
        }
        record Plus(Expr a, Expr b) implements Expr {
            @Override
            public String toString() {
                return String.format("(%s + %s)", a, b);
            }
        }
        record Minus(Expr a, Expr b) implements Expr {
            @Override
            public String toString() {
                return String.format("(%s - %s)", a, b);
            }
        }
        record Times(Expr a, Expr b) implements Expr {
            @Override
            public String toString() {
                return String.format("(%s * %s)", a, b);
            }
        }
        record Div(Expr a, Expr b) implements Expr {
            @Override
            public String toString() {
                return String.format("(%s / %s)", a, b);
            }
        }
    }

    public static void main(String[] args) {
//        runExample();
        AdventOfCodeRunner.run(new Day21());
    }

    private static void runExample() {
        AdventOfCodeRunner.example(new Day21(), """
            root: pppw + sjmn
            dbpl: 5
            cczh: sllz + lgvd
            zczc: 2
            ptdq: humn - dvpt
            dvpt: 3
            lfqf: 4
            humn: 5
            ljgn: 2
            sjmn: drzm * dbpl
            sllz: 4
            pppw: cczh / lfqf
            lgvd: ljgn * ptdq
            drzm: hmdt - zczc
            hmdt: 32""");
    }
}
