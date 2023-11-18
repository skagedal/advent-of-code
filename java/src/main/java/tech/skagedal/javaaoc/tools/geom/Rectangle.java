package tech.skagedal.javaaoc.tools.geom;

import static tech.skagedal.javaaoc.tools.streamsetc.Streams.iterate;

import java.util.stream.Stream;

public record Rectangle(Point origin, Size size) {
    public static Rectangle enclosing(Stream<Point> points) {
        int startX = Integer.MAX_VALUE;
        int endX = Integer.MIN_VALUE;
        int startY = Integer.MAX_VALUE;
        int endY = Integer.MIN_VALUE;
        for (var point : iterate(points)) {
            startX = Math.min(startX, point.x());
            endX = Math.max(endX, point.x());
            startY = Math.min(startY, point.y());
            endY = Math.max(endY, point.y());
        }
        return new Rectangle(
            new Point(startX, startY),
            new Size(endX - startX + 1, endY - startY + 1)
        );
    }

    public Stream<Point> allPoints() {
        return origin.interpolateTo(origin.plus(new Vector(size.width() - 1, 0)))
            .flatMap(p -> p.interpolateTo(p.plus(new Vector(0, size.height() - 1))));
    }
}
