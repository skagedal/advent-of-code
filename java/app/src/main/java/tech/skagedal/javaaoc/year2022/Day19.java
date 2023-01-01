package tech.skagedal.javaaoc.year2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Not Enough Minerals"
)
public class Day19 {

    public long part1(AdventContext context) {
        return 0;
    }

    private long maxGeodes(Blueprint blueprint) {
        return maxGeodes(
            blueprint,
            24,
            Map.of(ResourceType.ORE, 1),
            Map.of()
        );
    }

    private long maxGeodes(Blueprint blueprint, int minutes, Map<ResourceType, Integer> machines, Map<ResourceType, Integer> resources) {
        if (minutes == 0) {
            return resources.getOrDefault(ResourceType.GEODE, 0);
        }
        // The options are:
        //  1. buy nothing
        //  2. buy a number of robots.
        //     But should we support buying any number of robots? Or is it ok to just buy one at a time?
        return 0;
    }

    static class Simulation {
        private final Blueprint blueprint;
        private final int minutes;
        private final Map<ResourceType, Integer> machines = new HashMap<>();
        private final Map<ResourceType, Integer> resources = new HashMap<>();

        Simulation(Blueprint blueprint, int minutes) {
            this.blueprint = blueprint;
            this.minutes = minutes;
        }

        long largestNumberOfGeodes() {
            // We get this one for free.
            deployMachines(List.of(new Machine(ResourceType.ORE, List.of())));
            for (var minute = 0; minute < minutes; minute++) {
                System.out.printf("== Minute %d ==\n", minute + 1);
                final var boughtMachines = buyMachines();
                collectProduction();
                deployMachines(boughtMachines);
            }
            return resources.getOrDefault(ResourceType.GEODE, 0);
        }

        private List<Machine> buyMachines() {
            // This one is the tricky one, the actual problem to solve. I'm not sure how.
            // Gonna just play with some stupid greedy algorithm.
            for (var resourceType : ResourceType.BY_PRIORITY) {
                final var machine = findMachine(resourceType);
                if (canAfford(machine.price())) {
                    withdraw(machine.price());
                    return List.of(machine);
                }
            }

            // Clearly not the thing to do.

            return List.of();
        }

        private Machine findMachine(ResourceType resourceType) {
            return blueprint.machines().stream()
                .filter(m -> m.resourceType.equals(resourceType)).findFirst().orElseThrow();
        }

        private boolean canAfford(List<Price> price) {
            return price.stream().allMatch(p -> resourceCount(p.resourceType()) >= p.amount());
        }

        void withdraw(List<Price> price) {
            for (var p : price) {
                withdrawResource(p.amount(), p.resourceType());
            }
        }

        void withdrawResource(int amount, ResourceType resourceType) {
            resources.put(resourceType, resources.getOrDefault(resourceType, 0) - amount);
        }

        private int resourceCount(ResourceType resourceType) {
            return resources.getOrDefault(resourceType, 0);
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

        final var lnog = new Simulation(
            new Blueprint(1, List.of(
                new Machine(ResourceType.ORE, List.of(new Price(4, ResourceType.ORE))),
                new Machine(ResourceType.CLAY, List.of(new Price(2, ResourceType.ORE))),
                new Machine(ResourceType.OBSIDIAN, List.of(new Price(3, ResourceType.ORE), new Price(14, ResourceType.CLAY))),
                new Machine(ResourceType.GEODE, List.of(new Price(2, ResourceType.ORE), new Price(7, ResourceType.OBSIDIAN)))
            )),
            24
        ).largestNumberOfGeodes();

        // A geode robot costs 2 ore and 7 obsidian.
        // As soon as we have that, we should make sure to by a geode robot.
    }

}
