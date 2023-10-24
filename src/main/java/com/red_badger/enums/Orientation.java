package com.red_badger.enums;

import com.red_badger.exception.UnknownOrientationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Orientation {
    N("N", 0, 1),
    S("S", 0, -1),
    E("E", 1, 0),
    W("W", -1, 0);

    private final String orientation;
    private final int xIncrement;
    private final int yIncrement;

    public static Orientation fromString(final String orientation) {
        return Stream.of(Orientation.values())
                .filter(o -> o.orientation.equalsIgnoreCase(orientation))
                .findFirst()
                .orElseThrow(() -> new UnknownOrientationException(orientation));
    }

    public Orientation turnLeft() {
        return switch (this) {
            case N -> W;
            case S -> E;
            case E -> N;
            case W -> S;
        };
    }

    public Orientation turnRight() {
        return switch (this) {
            case N -> E;
            case S -> W;
            case E -> S;
            case W -> N;
        };
    }
}
