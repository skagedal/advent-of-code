package tech.skagedal.javaaoc.year2022;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Longs;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;
import tech.skagedal.javaaoc.tools.string.Strings;

@AdventOfCode(
    description = "Proboscidea Volcanium"
)
public class Day16 {
    public long part1(AdventContext context) {
        final var graph = createValveGraph(readValves(context));
        final var interestingValves = findInterestingValves(graph);
        final var startValve = findStartValve(graph);

        final var finder = new MaxPressureFinder(graph);
        final var solution = finder.maxPressure(startValve, 30, interestingValves);

        return solution.pressure();
    }

    public long part2(AdventContext context) {
        final var graph = createValveGraph(readValves(context));
        final var interestingValves = findInterestingValves(graph);
        final var startValve = findStartValve(graph);

        final var finder = new MaxPressureFinder(graph);
        final var solution = finder.maxPressure(startValve, 26, interestingValves);

        // Now, here comes the elephant.
        final var claimedValves = solution.path().toStream().collect(Collectors.toSet());
        final var elephantSolution = finder.maxPressure(
            startValve, 26, interestingValves.stream().filter(v -> !claimedValves.contains(v)).toList());

        return solution.pressure() + elephantSolution.pressure();
    }

    private static List<Valve> findInterestingValves(Graph<Valve, DefaultEdge> graph) {
        return graph.vertexSet().stream()
            .sorted(Comparator.comparing(Valve::flowRate).reversed())
            .takeWhile(v -> v.flowRate > 0)
            .toList();
    }

    private static Valve findStartValve(Graph<Valve, DefaultEdge> graph) {
        return graph.vertexSet().stream().filter(v -> v.id().equals("AA")).findFirst().orElseThrow();
    }

    static class MaxPressureFinder {
        private final FloydWarshallShortestPaths<Valve, DefaultEdge> shortestPaths;
        private final Map<String, Solution> cache = new HashMap<>();

        MaxPressureFinder(Graph<Valve, DefaultEdge> graph) {
            this.shortestPaths = new FloydWarshallShortestPaths<>(graph);
        }

        Solution maxPressure(Valve valve, long minutes, List<Valve> relevantValves) {
            if (minutes < 1) {
                return Solution.NONE;
            }

            final var cacheKey = String.format("%s-%d-%s", valve.id, minutes, relevantValves.stream().map(Valve::id).collect(Collectors.joining()));
            final var cachedValue = cache.get(cacheKey);
            if (cachedValue != null) {
                return cachedValue;
            }

            final var best = relevantValves.stream()
                .map(nextValve -> {
                    final var distance = shortestPaths.getPath(valve, nextValve).getLength();
                    final var flowTime = minutes - distance - 1;
                    if (flowTime > 0) {
                        final var valveScore = flowTime * nextValve.flowRate();
                        final var restSolution = maxPressure(nextValve, flowTime, relevantValves.stream().filter(v -> v != nextValve).toList());
                        return new Solution(valveScore + restSolution.pressure(), new Lst.Cons<>(nextValve, restSolution.path()));
                    } else {
                        return Solution.NONE;
                    }
                })
                .max(Comparator.comparing(Solution::pressure))
                .orElse(Solution.NONE);

            cache.put(cacheKey, best);
            return best;
        }
    }

    record Solution(long pressure, Lst<Valve> path) {
        final static Solution NONE = new Solution(0, new Lst.Nil<>());
    }

    sealed interface Lst<T> {
        default Stream<T> toStream() {
            return Streams.make(channel -> {
                var lst = this;
                while (lst instanceof Lst.Cons<T> cons) {
                    channel.yield(cons.head);
                    lst = cons.tail;
                }
            });
        }
        record Nil<T>() implements Lst<T> {}
        record Cons<T>(T head, Lst<T> tail) implements Lst<T> {}
    }


    private static Graph<Valve, DefaultEdge> createValveGraph(Map<String, Valve> valveSpec) {
        Graph<Valve, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (var spec : valveSpec.values()) {
            graph.addVertex(spec);
        }
        for (var spec : valveSpec.values()) {
            for (var target : spec.tunnels) {
                graph.addEdge(spec, valveSpec.get(target));
            }
        }
        return graph;
    }

    private static Map<String, Valve> readValves(AdventContext context) {
        return context.lines().map(Valve::fromString).collect(Collectors.toMap(
            Valve::id,
            Function.identity()
        ));
    }

    record Valve(String id, long flowRate, List<String> tunnels) {
        private static final Pattern valvePattern = Pattern.compile("[A-Z]{2}");
        static Valve fromString(String s) {
            final List<String> valves = Strings.inString(s, valvePattern).toList();
            final var flowRate = Longs.inString(s).findFirst().orElseThrow();
            return new Valve(valves.get(0), flowRate, valves.subList(1, valves.size()));
        }

        @Override
        public String toString() {
            return id;
        }
    }

    public static void main(String[] args) {
        runExample();
        runMain();
    }

    private static void runExample() {
        AdventOfCodeRunner.example(new Day16(), """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II""");
    }

    private static void runMain() {
        AdventOfCodeRunner.run(new Day16());
    }
}
