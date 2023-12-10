package tech.skagedal.javaaoc.tools.geom;

import java.util.stream.Stream;

public enum Direction4 {
    PLUS_X,
    MINUS_X,
    PLUS_Y,
    MINUS_Y;

    public static Stream<Direction4> allFour() {
        return Stream.of(PLUS_X, MINUS_X, PLUS_Y, MINUS_Y);
    }

    public Vector vector() {
        return switch (this) {
            case PLUS_X -> Vector.RIGHT;
            case MINUS_X -> Vector.LEFT;
            case PLUS_Y -> Vector.DOWN;
            case MINUS_Y -> Vector.UP;
        };
    }
}
