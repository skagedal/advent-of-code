package tech.skagedal.javaaoc.tools.linear;

public record Vector(int dx, int dy) {
    public Point addTo(Point point) { return new Point(point.x() + dx, point.y() + dy);
    }
}
