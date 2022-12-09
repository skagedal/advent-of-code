package tech.skagedal.javaaoc.tools.linear;

public record Point(int x, int y) {
    public Point plus(Vector vector) {
        return new Point(x + vector.dx(), y + vector.dy());
    }

    public Vector minus(Point p) {
        return new Vector(x - p.x, y - p.y);
    }
}
