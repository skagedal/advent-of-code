package tech.skagedal.javaaoc.year2015;

import java.util.HashMap;
import tech.skagedal.javaaoc.aoc.AdventContext;
import tech.skagedal.javaaoc.aoc.AdventOfCode;
import tech.skagedal.javaaoc.tools.collections.Counter;
import tech.skagedal.javaaoc.tools.geom.Point;

@AdventOfCode(
    description = "Perfectly Spherical Houses in a Vacuum"
)
public class Day03 {
    public long part1(AdventContext context) {
        var counts = new Counter<Point>();
        var point = Point.ZERO;

        counts.increase(point);

        // TODO: Go through the points...

        return 0;
    }
}
