package tech.skagedal.javaaoc.year2015;

import java.util.List;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.geom.Size3D;
import tech.skagedal.javaaoc.tools.math.Ints;

@AdventOfCode(
    description = "I Was Told There Would Be No Math"
)
public class Day02 {
    public long part1(AdventContext context) {
        return context.lines()
            .mapToInt(line -> wrappingNeeded(Ints.inString(line, Size3D::new)))
            .sum();
    }

    public long part2(AdventContext context) {
        return context.lines()
            .map(line -> Ints.inString(line, Size3D::new))
            .mapToInt(this::ribbonNeeded)
            .sum();
    }

    private int wrappingNeeded(Size3D size) {
        final var sides = List.of(
            size.width() * size.height(),
            size.width() * size.depth(),
            size.height() * size.depth()
        );
        return sides.stream().mapToInt(side -> side * 2).sum() +
            sides.stream().mapToInt(Integer::intValue).min().orElseThrow();
    }

    private int ribbonNeeded(Size3D size) {
        final var perimeters = List.of(
            size.width() * 2 + size.height() * 2,
            size.width() * 2 + size.depth() * 2,
            size.height() * 2 + size.depth() * 2
        );
        return perimeters.stream().mapToInt(Integer::intValue).min().orElseThrow() + size.volume();
    }

}
