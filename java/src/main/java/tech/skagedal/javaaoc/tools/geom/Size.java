package tech.skagedal.javaaoc.tools.geom;

public record Size(int width, int height) {
    public Vector toVector() {
        return new Vector(width, height);
    }
}
