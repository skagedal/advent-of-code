package tech.skagedal.javaaoc.tools.geom;

public record Vector3D(int dx, int dy, int dz) {
    public Point3D addTo(Point3D point) {
        return point.plus(this);
    }
}
