package tech.skagedal.javaaoc.year2023;

import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.aoc.AdventOfCodeRunner;
import tech.skagedal.javaaoc.tools.math.Longs;
import tech.skagedal.javaaoc.tools.streamsetc.Streams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@AdventOfCode(
    description = "If You Give A Seed A Fertilizer"
)
public class Day05 {
    public long part1(AdventContext context) {
        var chunks = Streams.splitting(context.lines(), String::isBlank).toList();
        var seeds = Longs.inString(chunks.getFirst().getLast()).toList();
        var maps = chunks.stream().skip(1).map(Map::parse).toList();
        return findLowestLocation(seeds.stream().mapToLong(Long::longValue), maps);
    }

    public long part2(AdventContext context) {
        var chunks = Streams.splitting(context.lines(), String::isBlank).toList();
        var seeds = Longs.inString(chunks.getFirst().getLast()).toList();
        var maps = chunks.stream().skip(1).map(Map::parse).toList();

        return findLowestLocation(seedsInRanges(seeds.stream()), maps);
    }

    private LongStream seedsInRanges(Stream<Long> stream) {
        return Streams.splittingFixedSize(stream, 2)
            .flatMapToLong(list -> LongStream.range(list.getFirst(), list.getFirst() + list.getLast()));
    }

    private long findLowestLocation(LongStream stream, List<Map> maps) {
        return stream
            .parallel()
            .map(seed -> findLocation(seed, maps))
            .min()
            .orElseThrow();
    }

    private long findLocation(long seed, List<Map> maps) {
        var output = seed;
        for (var map : maps) {
            output = map.map(output);
        }
        return output;
    }

    public record Map(
        List<MapRange> ranges
    ) {
        static Map parse(List<String> strings) {
            return new Map(strings.stream().skip(1)
                .map(MapRange::parse)
                .sorted(Comparator.comparingLong(MapRange::sourceStart))
                .toList());
        }

        public long map(long value) {
            for (var range : ranges) {
                if (value >= range.sourceStart) {
                    if (value < (range.sourceStart + range.rangeLength)) {
                        return value + range.diff;
                    }
                } else {
                    break;
                }
            }
            return value;
        }

        public Map then(Map map) {
            return new Map(Stream.concat(ranges().stream(), map.ranges().stream()).toList());
        }
    }

    public record MapRange(
       long destinationStart,
       long sourceStart,
       long rangeLength,
       long diff
    ) {
        public static MapRange parse(String s) {
            var longs = Longs.inString(s).mapToLong(Long::longValue).toArray();
            return create(longs[1], longs[0], longs[2]);
        }

        public static MapRange create(long sourceStart, long destinationStart, long rangeLength) {
            return new MapRange(destinationStart, sourceStart, rangeLength, destinationStart - sourceStart);
        }
    }

    public static void main(String[] args) {
        AdventOfCodeRunner.example(new Day05(), """
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4""");
        AdventOfCodeRunner.run(new Day05());
    }
}
