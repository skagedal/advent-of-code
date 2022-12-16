package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Longs;
import tech.skagedal.javaaoc.tools.string.Strings;

@AdventOfCode(
    description = "Proboscidea Volcanium"
)
public class Day16 {
    public long part1(AdventContext context) {
        final var valves = readValves(context);
        final var finder = new PressureFinder(valves, context.explain());
        return finder.maxPressure(valves.get("AA"), 30, Set.of());
    }

    public long part2(AdventContext context) {
        final var valves = readValves(context);
        final var finder = new PressureFinder(valves, context.explain());
        return finder.maxPressureWithElephant(valves.get("AA"), valves.get("AA"), 26, Set.of());
    }

    private static Map<String, Valve> readValves(AdventContext context) {
        return context.lines().map(Valve::fromString).collect(Collectors.toMap(
            Valve::id,
            Function.identity()
        ));
    }

    static class PressureFinder {
        private final Map<String, Valve> valves;
        private final boolean explain;
        private Map<String, Long> cache = new HashMap<>();

        PressureFinder(Map<String, Valve> valves, boolean explain) {
            this.valves = valves;
            this.explain = explain;
        }

        long maxPressure(Valve valve, int minutes, Set<String> openValves) {
            if (explain) System.out.printf("== Considering valve %s at %d minutes left ==\n", valve.id, minutes);
            if (minutes == 0) {
                return 0;
            }

            final var cacheKey = String.format("%s-%d-%s", valve.id, minutes, openValves.stream().sorted().collect(Collectors.joining()));
            final var cachedValue = cache.get(cacheKey);
            if (cachedValue != null) {
                return cachedValue;
            }

            final var id = String.format("%s:%02d", valve.id, minutes);
            Map<String, Long> scoreOptions = new HashMap<>();

            if (valve.flowRate == 0) {
                if (explain) System.out.printf("[%s] No point in opening, no flow\n", id);
            } else if (openValves.contains(valve.id)) {
                if (explain) System.out.printf("[%s] Already open\n", id);
            } else {
                if (explain) System.out.printf("[%s] We could open this vault. It takes one minute to open.\n", id);
                long scoreForOpening = (minutes - 1) * valve.flowRate;
                if (explain) System.out.printf("[%s] So the score for just this would be %d\n", id, scoreForOpening);
                long scoreForRest = maxPressure(valve, minutes - 1, Sets.union(openValves, Set.of(valve.id)));
                scoreOptions.put(valve.id, scoreForOpening + scoreForRest);
            }

            for (var otherValve : valve.tunnels) {
                final var scoreForMoving = maxPressure(valves.get(otherValve), minutes - 1, openValves);
                scoreOptions.put(otherValve, scoreForMoving);
            }

            final var maxOption = scoreOptions.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElseThrow();
            if (explain) System.out.printf("[%s] We chose option %s, worth %d\n", id, maxOption.getKey(), maxOption.getValue());
            cache.put(cacheKey, maxOption.getValue());
            return maxOption.getValue();
        }

        long maxPressureWithElephant(Valve valve, Valve elephantsValve, int minutes, Set<String> openValves) {
            if (explain) System.out.printf("== Considering valve %s at %d minutes left ==\n", valve.id, minutes);
            if (minutes == 0) {
                return 0;
            }

            final var cacheKey = String.format("%s-%d-%s", valve.id, minutes, openValves.stream().sorted().collect(Collectors.joining()));
            final var cachedValue = cache.get(cacheKey);
            if (cachedValue != null) {
                return cachedValue;
            }

            final var id = String.format("%s:%02d", valve.id, minutes);
            Map<String, Long> scoreOptions = new HashMap<>();

            if (valve.flowRate == 0) {
                if (explain) System.out.printf("[%s] No point in opening, no flow\n", id);
            } else if (openValves.contains(valve.id)) {
                if (explain) System.out.printf("[%s] Already open\n", id);
            } else {
                if (explain) System.out.printf("[%s] We could open this vault. It takes one minute to open.\n", id);
                long scoreForOpening = (minutes - 1) * valve.flowRate;
                if (explain) System.out.printf("[%s] So the score for just this would be %d\n", id, scoreForOpening);
                long scoreForRest = maxPressure(valve, minutes - 1, Sets.union(openValves, Set.of(valve.id)));
                scoreOptions.put(valve.id, scoreForOpening + scoreForRest);
            }

            for (var otherValve : valve.tunnels) {
                final var scoreForMoving = maxPressure(valves.get(otherValve), minutes - 1, openValves);
                scoreOptions.put(otherValve, scoreForMoving);
            }

            final var maxOption = scoreOptions.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElseThrow();
            if (explain) System.out.printf("[%s] We chose option %s, worth %d\n", id, maxOption.getKey(), maxOption.getValue());
            cache.put(cacheKey, maxOption.getValue());
            return maxOption.getValue();
        }
    }

    record Valve(String id, long flowRate, List<String> tunnels) {
        private static Pattern valvePattern = Pattern.compile("[A-Z]{2}");
        static Valve fromString(String s) {
            final List<String> valves = Strings.inString(s, valvePattern).toList();
            final var flowRate = Longs.inString(s).findFirst().orElseThrow();
            return new Valve(valves.get(0), flowRate, valves.subList(1, valves.size()));
        }
    }

    public static void main(String[] args) {
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
//        AdventOfCodeRunner.run(new Day16());
    }
}
