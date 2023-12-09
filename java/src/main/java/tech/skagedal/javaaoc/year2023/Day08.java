package tech.skagedal.javaaoc.year2023;

import org.springframework.context.event.SourceFilteringListener;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.function.Tuple2;
import tech.skagedal.javaaoc.tools.function.Tuple3;
import tech.skagedal.javaaoc.tools.math.Maths;
import tech.skagedal.javaaoc.tools.regex.Patterns;
import tech.skagedal.javaaoc.tools.streamsetc.Enumerated;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.*;
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
        var edges = nodeMap.stream().map(Day08::edgeDescription).collect(Collectors.toMap(
            List::getFirst,
            list -> new Tuple2<>(list.get(1), list.get(2))
        ));

        long steps = 0;
        var node = "AAA";
        while (!node.equals("ZZZ")) {
            var instruction = instructions.charAt((int)steps % (instructions.length()));
            var edge = edges.get(node);
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
        var sections = Streams.splitting(context.lines(), String::isBlank).toList();
        var instructions = sections.getFirst().getFirst();
        var nodeMap = sections.getLast();
        var edges = nodeMap.stream().map(Day08::edgeDescription).collect(Collectors.toMap(
            List::getFirst,
            list -> new Tuple2<>(list.get(1), list.get(2))
        ));

        System.out.printf("Number of instructions: %d\n", instructions.length());
        System.out.printf("Number of edges: %d\n", edges.keySet().size());

        long steps = 0;
        var currentNodes = findStartNodes(edges);
        System.out.printf("Start nodes: %d\n", currentNodes.length);

        var finder = new Finder(instructions, edges);
        for (var i = 0; i < currentNodes.length; i++) {
            finder.findLoop(currentNodes[i]);
        }

        var endStates = Arrays.stream(currentNodes).mapToInt(finder::findLoop).toArray();
        System.out.printf("LCD of %s\n", Arrays.stream(endStates).mapToObj(String::valueOf).collect(Collectors.joining(", ")));

        long s = endStates[0];
        for (var i = 1; i < endStates.length; i++) {
            s = Maths.lcm(s, endStates[i]);
        }
        return s;
    }

    static class Finder {
        private final String instructions;
        private final Map<String, Tuple2<String, String>> edges;

        record State(int instruction, String node) {
            boolean isEndNode() {
                return node.endsWith("Z");
            }
        }
        Finder(String instructions, Map<String, Tuple2<String, String>> edges) {
            this.instructions = instructions;
            this.edges = edges;
        }

        int findLoop(String node) {
            int loopLength = 0;
            var seenStates = new HashMap<State, Integer>();
            var seenStatesList = new ArrayList<State>();
            var current = new State(0, node);
            while (!seenStates.containsKey(current)) {
                seenStates.put(current, loopLength);
                seenStatesList.add(current);
                var instructionCharacter = instructions.charAt(current.instruction);
                var edge = edges.get(current.node);
                var nextNode = switch (instructionCharacter) {
                    case 'L' -> edge.value1();
                    case 'R' -> edge.value2();
                    default -> throw new IllegalArgumentException();
                };
                current = new State((current.instruction + 1) % instructions.length(), nextNode);
                loopLength += 1;
            }

            System.out.printf(" - Loop starting with %s has length %d, ends with %s\n", seenStatesList.getFirst(), seenStatesList.size(), seenStatesList.getLast());
            System.out.printf("   Then it starts over with %s.\n", current);
            final var realLoopLength = seenStatesList.size() - current.instruction();
            System.out.printf("   So, the loop length is %d.\n", realLoopLength);
            System.out.printf("   We find end states at " + Streams.enumerated(seenStatesList.stream()).filter(es -> es.value().isEndNode()).map(Enumerated::number).map(String::valueOf).collect(Collectors.joining(", ")) + "\n");
            final var endState = Streams.enumerated(seenStatesList.stream()).filter(es -> es.value().isEndNode()).mapToInt(Enumerated::number).findAny().orElseThrow();
            System.out.printf("   Assume only one. It's at %d.\n", endState);
            System.out.printf("   So, we will have end states at every i = %d + (%d * k)\n", endState, realLoopLength);
            return endState;
        }
    }

    private String[] findStartNodes(Map<String, Tuple2<String, String>> edges) {
        return edges.keySet().stream().filter(s -> s.endsWith("A")).toArray(String[]::new);
    }

    private boolean isEndNodes(String[] currentNodes) {
        return Arrays.stream(currentNodes).allMatch(s -> s.endsWith("Z"));
    }

    static Pattern NODE_PATTERN = Pattern.compile("[A-Z0-9]{3}");
    private static List<String> edgeDescription(String s) {
        return NODE_PATTERN.matcher(s).results().map(MatchResult::group).toList();
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.run(new Day08());
    }
}
