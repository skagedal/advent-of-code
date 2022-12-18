package tech.skagedal.javaaoc.tools.geom;

public record Vector(int dx, int dy) {
    public Point addTo(Point point) {
        return point.plus(this);
    }

    public Vector minus(Vector v) {
        return new Vector(dx - v.dx, dy - v.dy);
    }

    public boolean isNonZero() {
        return dx != 0 || dy != 0;
    }

    public Vector dividedBy(int n) {
        return new Vector(dx / n, dy / n);
    }

    public Vector times(int n) {
        return new Vector(dx * n, dy * n);
    }
}
