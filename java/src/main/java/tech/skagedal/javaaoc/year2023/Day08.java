package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.function.Tuple3;
import tech.skagedal.javaaoc.tools.regex.Patterns;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AdventOfCode(
    description = "Haunted Wasteland"
)
public class Day08 {
    public long part1(AdventContext context) {
        var sections = Streams.splitting(context.lines(), String::isBlank).toList();
        var instructions = sections.getFirst().getFirst();
        var nodeMap = sections.getLast();
        var nodes = nodeMap.stream().map(Day08::edgeDescription).collect(Collectors.toMap(
            List::getFirst,
            list -> new Tuple2<>(list.get(1), list.get(2))
        ));

        long steps = 0;
        var node = "AAA";
        while (!node.equals("ZZZ")) {
            var instruction = instructions.charAt((int)steps % (instructions.length()));
            var edge = nodes.get(node);
            node = switch (instruction) {
                case 'L' -> edge.value1();
                case 'R' -> edge.value2();
                default -> throw new IllegalArgumentException();
            };
            steps++;
        }
        return steps;
    }

    public long part2(AdventContext context) {
        return 0;
    }

    static Pattern NODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static List<String> edgeDescription(String s) {
        return NODE_PATTERN.matcher(s).results().map(MatchResult::group).toList();
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day08(), """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)""");

        AdventOfCodeRunner.example(new Day08(), """
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)""");
        AdventOfCodeRunner.run(new Day08());
    }
}
