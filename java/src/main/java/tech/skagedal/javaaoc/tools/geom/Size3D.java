package tech.skagedal.javaaoc.tools.geom;

public record Size3D(int width, int height, int depth) {
    public Vector3D toVector() {
        return new Vector3D(width, height, depth);
    }

    public int volume() {
        return width * height * depth;
    }
}
