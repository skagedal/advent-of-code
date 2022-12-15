package tech.skagedal.javaaoc.year2022;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.linear.Point;
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
        final var pairs = context.lines().map(SensorBeaconPair::fromLine).toList();

        // TODO: This could be way quicker, using same method as in part 2
        // Scan the line, starting at the X position where some sensor could at most reach.
        final var leftmost = pairs.stream().mapToLong(SensorBeaconPair::leftmostReach).min().orElseThrow();
        final var rightmost = pairs.stream().mapToLong(SensorBeaconPair::rightmostReach).max().orElseThrow();

        final var pointStart = new Point((int) leftmost, part1LineY);
        final var pointEnd = new Point((int) rightmost, part1LineY);

        return pointStart.interpolateTo(pointEnd)
            .filter(point ->
                pairs.stream().noneMatch(sensor -> sensor.beacon().equals(point)) &&
                    pairs.stream().anyMatch(sensor -> sensor.reaches(point))
            )
            .count();
    }

    public long part2(AdventContext context) {
        final var sensors = context.lines()
            .map(SensorBeaconPair::fromLine)
            .map(Sensor::fromSensorBeaconPair)
            .sorted(Comparator.comparing(Sensor::range).reversed())
            .toList();

        final var fullWidth = Range.closed(0L, (long)part2SearchSize);
        for (var y = 0; y <= part2SearchSize; y++) {
            final var rangeSet = buildSensorRangeSetForLine(sensors, y);
            if (!rangeSet.encloses(fullWidth)) {
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

        int leftmostReach() {
            return sensor.x() - range();
        }

        int rightmostReach() {
            return sensor.x() + range();
        }

        boolean reaches(Point point) {
            return sensor.manhattanDistanceTo(point) <= range();
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
