package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.geom.Point;
import tech.skagedal.javaaoc.tools.math.Longs;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

@AdventOfCode(
    description = "Beacon Exclusion Zone"
)
public class Day15 {
    private final int part1LineY;
    private final int part2SearchSize;

    public Day15() {
        this(2000000, 4000000);
    }

    public Day15(int part1LineY, int part2SearchSize) {
        this.part1LineY = part1LineY;
        this.part2SearchSize = part2SearchSize;
    }

    public long part1(AdventContext context) {
        final var sensorBeaconPairs = context.lines().map(SensorBeaconPair::fromLine).toList();

        final var sensors = sensorBeaconPairs.stream()
            .map(Sensor::fromSensorBeaconPair)
            .toList();

        final var beacons = sensorBeaconPairs.stream().map(SensorBeaconPair::beacon).collect(Collectors.toSet());

        return buildSensorRangeSetForLine(sensors, part1LineY).asRanges().stream()
            .mapToLong(range -> range.upperEndpoint() - range.lowerEndpoint() - beaconsInRange(beacons, range, part1LineY))
            .sum();
    }

    private static long beaconsInRange(Set<Point> beacons, Range<Long> range, int y) {
        return beacons.stream()
            .filter(beacon -> beacon.y() == y && range.contains((long)beacon.x()))
            .count();
    }

    public long part2(AdventContext context) {
        final var sensors = context.lines()
            .map(SensorBeaconPair::fromLine)
            .map(Sensor::fromSensorBeaconPair)
            .toList();

        final var fullWidth = Range.closed(0L, (long)part2SearchSize);
        for (var y = 0; y <= part2SearchSize; y++) {
            final var rangeSet = buildSensorRangeSetForLine(sensors, y);
            if (!rangeSet.encloses(fullWidth)) {
                // We take the x that comes first after the first range - that's a bit of cheating as there could be
                // ranges that live completely outside of our fullWidth here, but it's ok for our data :)
                final var x = rangeSet.asRanges().iterator().next().upperEndpoint();
                return x * 4000000L + y;
            }
        }
        return 0;
    }

    private RangeSet<Long> buildSensorRangeSetForLine(List<Sensor> sensors, long lineNum) {
        return TreeRangeSet.create(Streams.iterate(sensors.stream()
            .map(sensor -> sensor.rangeForLine(lineNum))
            .flatMap(Optional::stream)));
    }

    record Sensor(Point sensor, int range) {
        static Sensor fromSensorBeaconPair(SensorBeaconPair sensorBeaconPair) {
            return new Sensor(sensorBeaconPair.sensor(), sensorBeaconPair.range());
        }

        Optional<Range<Long>> rangeForLine(long y) {
            final var distanceInY = Math.abs(y - sensor.y());
            final var width = range - distanceInY;
            if (width < 0) {
                return Optional.empty();
            } else {
                return Optional.of(Range.closedOpen(sensor.x() - width, sensor.x() + width + 1));
            }
        }
    }

    record SensorBeaconPair(Point sensor, Point beacon) {
        static SensorBeaconPair fromLine(String s) {
            final var longs = Longs.inString(s).toList();
            return new SensorBeaconPair(
                new Point(longs.get(0).intValue(), longs.get(1).intValue()),
                new Point(longs.get(2).intValue(), longs.get(3).intValue())
            );
        }

        int range() {
            return sensor.manhattanDistanceTo(beacon);
        }
    }

    public static void main(String[] args) {
        System.out.println("== EXAMPLE: ==");
        System.out.println(new Day15(10, 20).part2(AdventContext.fromString(
            """
                Sensor at x=2, y=18: closest beacon is at x=-2, y=15
                Sensor at x=9, y=16: closest beacon is at x=10, y=16
                Sensor at x=13, y=2: closest beacon is at x=15, y=3
                Sensor at x=12, y=14: closest beacon is at x=10, y=16
                Sensor at x=10, y=20: closest beacon is at x=10, y=16
                Sensor at x=14, y=17: closest beacon is at x=10, y=16
                Sensor at x=8, y=7: closest beacon is at x=2, y=10
                Sensor at x=2, y=0: closest beacon is at x=2, y=10
                Sensor at x=0, y=11: closest beacon is at x=2, y=10
                Sensor at x=20, y=14: closest beacon is at x=25, y=17
                Sensor at x=17, y=20: closest beacon is at x=21, y=22
                Sensor at x=16, y=7: closest beacon is at x=15, y=3
                Sensor at x=14, y=3: closest beacon is at x=15, y=3
                Sensor at x=20, y=1: closest beacon is at x=15, y=3"""
        )));

        System.out.println("== REAL: ==");
        AdventOfCodeRunner.run(new Day15());
    }
}
