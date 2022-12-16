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

@AdventOfCode
public class Day16 {
    Map<String, Long> cache = new HashMap<>();

    public long part1(AdventContext context) {
        cache.clear();
        final var valves = context.lines().map(Valve::fromString).collect(Collectors.toMap(
            Valve::id,
            Function.identity()
        ));
        return maxPressure(valves.get("AA"), valves, 30, Set.of());
    }

    long maxPressure(Valve valve, Map<String, Valve> valves, int minutes, Set<String> openValves) {
        System.out.printf("== Considering valve %s at %d minutes left ==\n", valve.id, minutes);
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
            System.out.printf("[%s] No point in opening, no flow\n", id);
        } else if (openValves.contains(valve.id)) {
            System.out.printf("[%s] Already open\n", id);
        } else {
            System.out.printf("[%s] We could open this vault. It takes one minute to open.\n", id);
            long scoreForOpening = (minutes - 1) * valve.flowRate;
            System.out.printf("[%s] So the score for just this would be %d\n", id, scoreForOpening);
            long scoreForRest = maxPressure(valve, valves, minutes - 1, Sets.union(openValves, Set.of(valve.id)));
            scoreOptions.put(valve.id, scoreForOpening + scoreForRest);
        }

        for (var otherValve : valve.tunnels) {
            final var scoreForMoving = maxPressure(valves.get(otherValve), valves, minutes - 1, openValves);
            scoreOptions.put(otherValve, scoreForMoving);
        }

        final var maxOption = scoreOptions.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElseThrow();
        System.out.printf("[%s] We chose option %s, worth %d\n", id, maxOption.getKey(), maxOption.getValue());
        cache.put(cacheKey, maxOption.getValue());
        return maxOption.getValue();
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
        AdventOfCodeRunner.run(new Day16());
    }
}
