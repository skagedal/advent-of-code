package tech.skagedal.javaaoc.tools.geom;

import static tech.skagedal.javaaoc.tools.streamsetc.Streams.iterate;

import java.util.stream.Stream;

public record Rectangle3D(Point3D origin, Size3D size) {
    public static Rectangle3D enclosing(Stream<Point3D> points) {
        return enclosing(points, 0);
    }

    public static Rectangle3D enclosing(Stream<Point3D> points, int margin) {
        int startX = Integer.MAX_VALUE;
        int startY = Integer.MAX_VALUE;
        int startZ = Integer.MAX_VALUE;
        int endX = Integer.MIN_VALUE;
        int endY = Integer.MIN_VALUE;
        int endZ = Integer.MIN_VALUE;

        for (var point : iterate(points)) {
            startX = Math.min(startX, point.x());
            startY = Math.min(startY, point.y());
            startZ = Math.min(startZ, point.z());
            endX = Math.max(endX, point.x());
            endY = Math.max(endY, point.y());
            endZ = Math.max(endZ, point.z());
        }

        return new Rectangle3D(
            new Point3D(startX - margin, startY - margin, startZ - margin),
            new Size3D(
                endX - startX + 1 + margin * 2,
                endY - startY + 1 + margin * 2,
                endZ - startZ + 1 + margin * 2
            )
        );
    }
}
