package tech.skagedal.javaaoc.year2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Not Enough Minerals"
)
public class Day19 {

    static class Simulation {
        private final Blueprint blueprint;
        private final int minutes;
        private final Map<ResourceType, Integer> machines = new HashMap<>();
        private final Map<ResourceType, Integer> resources = new HashMap<>();

        Simulation(Blueprint blueprint, int minutes) {
            this.blueprint = blueprint;
            this.minutes = minutes;
        }

        void run() {
            // We get this one for free.
            deployMachines(List.of(new Machine(ResourceType.ORE, List.of())));
            for (var minute = 0; minute < minutes; minute++) {
                System.out.printf("== Minute %d ==\n", minute + 1);
                final var boughtMachines = buyMachines();
                collectProduction();
                deployMachines(boughtMachines);
            }
        }

        private List<Machine> buyMachines() {
            // This one is the tricky one.
            for (var resourceType : ResourceType.BY_PRIORITY) {

            }
            return List.of();
        }

        private void collectProduction() {
            for (var entry : machines.entrySet()) {
                final var resourceType = entry.getKey();
                final var num = entry.getValue();
                resources.put(resourceType, resources.getOrDefault(resourceType, 0) + 1);
                System.out.printf("%d %s-collecting robot collects %d %s, you now have %d %s\n",
                    num, resourceType, num, resourceType, resources.get(resourceType), resourceType
                );
            }
        }

        private void deployMachines(List<Machine> boughtMachines) {
            for (var resourceType : Streams.iterate(boughtMachines.stream().map(Machine::resourceType))) {
                machines.put(resourceType, machines.getOrDefault(resourceType, 0) + 1);
                System.out.printf("The new %s-collecting robot is ready, you now have %d of them.\n",
                    resourceType, machines.get(resourceType));
            }
        }
    }

    enum ResourceType {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE;

        static final List<ResourceType> BY_PRIORITY = List.of(GEODE, OBSIDIAN, CLAY, ORE);
    }

    record Price(int amount, ResourceType resourceType) {}
    record Machine(ResourceType resourceType, List<Price> price) {}
    record Blueprint(int id, List<Machine> machines) {
        Blueprint fromString(String s) {
            return new Blueprint(0, List.of());
        }
    }

    public static void main(String[] args) {
        final var example = """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
            """;

        new Simulation(
            new Blueprint(1, List.of(

            )),
            24
        ).run();
    }

}
