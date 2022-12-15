package tech.skagedal.javaaoc.year2022;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Stream;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeDay;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.aoc.DataLoaderFactory;
import tech.skagedal.javaaoc.tools.linear.Point;
import tech.skagedal.javaaoc.tools.linear.Rectangle;
import tech.skagedal.javaaoc.tools.linear.Size;
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

        // Scan the line, starting at the X position where some sensor could at most reach.
        final var leftmost = pairs.stream().mapToLong(SensorBeaconPair::leftmostReach).min().orElseThrow();
        final var rightmost = pairs.stream().mapToLong(SensorBeaconPair::rightmostReach).max().orElseThrow();

        System.out.printf("%d to %d (%d points)\n", leftmost, rightmost, rightmost - leftmost);

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
        final var pairs = context.lines()
            .map(SensorBeaconPair::fromLine)
            .map(Sensor::fromSensorBeaconPair)
            .sorted(Comparator.comparing(Sensor::range).reversed())
            .toList();

        System.out.printf("We have %d sensors.\n", pairs.size());
        System.out.printf("Sorted by range:\n");
        for (var sensor : Streams.iterate(pairs.stream())) {
            System.out.printf("- %d\n", sensor.range());
        }

        //        for (var y = 0; y <= part2SearchSize; y++) {
//            for (var x = 0; x <= part2SearchSize; x++) {
//
//            }
//        }

        final var rect = new Rectangle(Point.ZERO, new Size(part2SearchSize + 1, part2SearchSize + 1));
        final var start = Instant.now();
        final var distressSignalPoint = rect.allPoints()
            .peek(point -> {
                if (point.y() == 0) {
                    final var speedPerLine = Duration.between(start, Instant.now()).dividedBy(point.x() + 1);
                    final var finished = Instant.now().plus(speedPerLine.multipliedBy(part2SearchSize));
                    final var finishedTime = finished.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    System.out.printf("Line from %s - estimated finish at %s...\n", point, finishedTime);
                }
            })
            .filter(point -> pairs.stream().noneMatch(sensor -> sensor.reaches(point)))
            .findFirst().orElseThrow();

        return distressSignalPoint.x() * 4000000L + distressSignalPoint.y();
    }

    record Sensor(Point sensor, int range) {
        static Sensor fromSensorBeaconPair(SensorBeaconPair sensorBeaconPair) {
            return new Sensor(sensorBeaconPair.sensor(), sensorBeaconPair.range());
        }

        boolean reaches(Point point) {
            return sensor.manhattanDistanceTo(point) <= range;
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
        Day15 day15 = new Day15();
        final var p2 = day15.part2(new DataLoaderFactory().getDataLoader(AdventOfCodeDay.fromObject(day15)));
        System.out.println(p2);
//        AdventOfCodeRunner.run(new Day15());
    }
}
