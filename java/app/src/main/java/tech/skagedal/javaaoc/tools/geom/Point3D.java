package tech.skagedal.javaaoc.tools.geom;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public record Point3D(int x, int y, int z) {
    private static final Pattern pattern = Pattern.compile("(\\d+),(\\d+),(\\d+)");

    public static Stream<Point3D> parsePointsInString(String string) {
        return pattern.matcher(string).results().map(Point3D::fromMatchResult);
    }

    public static Point3D parseString(String string) {
        return parsePointsInString(string).findFirst().orElseThrow();
    }

    private static Point3D fromMatchResult(MatchResult matchResult) {
        return new Point3D(
            Integer.parseInt(matchResult.group(1)),
            Integer.parseInt(matchResult.group(2)),
            Integer.parseInt(matchResult.group(3))
        );
    }

    // Basic operations

    public Vector3D minus(Point3D p) {
        return new Vector3D(
            x - p.x,
            y - p.y,
            z - p.z
        );
    }

    public Point3D plus(Vector3D vector) {
        return new Point3D(
            x + vector.dx(),
            y + vector.dy(),
            z + vector.dz()
        );
    }
}
