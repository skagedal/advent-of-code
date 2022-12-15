package tech.skagedal.javaaoc.tools.linear;

public record Size(int width, int height) {
    public Vector toVector() {
        return new Vector(width, height);
    }
}
