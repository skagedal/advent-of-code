package tech.skagedal.javaaoc.tools.linear;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Point(int x, int y) {
    private static final Pattern pattern = Pattern.compile("(\\d+),(\\d+)");

    public static final Point ZERO = new Point(0, 0);

    /**
     * Finds all points of the format XX,YY in the string
     */
    public static Stream<Point> parsePointsInString(String string) {
        return pattern.matcher(string).results().map(Point::fromMatchResult);
    }

    public static Point parseString(String string) {
        return parsePointsInString(string).findFirst().orElseThrow();
    }

    private static Point fromMatchResult(MatchResult matchResult) {
        return new Point(
            Integer.parseInt(matchResult.group(1)),
            Integer.parseInt(matchResult.group(2))
        );
    }

    public Point plus(Vector vector) {
        return new Point(x + vector.dx(), y + vector.dy());
    }

    public Vector minus(Point p) {
        return new Vector(x - p.x, y - p.y);
    }

    // Note that this only handles straight lines or lines at 45 degrees
    public Stream<Point> interpolateTo(Point other) {
        final var length = Math.max(Math.abs(other.x - x), Math.abs(other.y - y));
        final var diff = other.minus(this).dividedBy(length);
        return IntStream.rangeClosed(0, length).mapToObj(i -> plus(diff.times(i)));
    }
}
