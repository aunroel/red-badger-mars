package com.red_badger.enums;

import com.red_badger.exception.UnknownOrientationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrientationTest {

    @ParameterizedTest
    @MethodSource("validOrientations")
    void whenValidOrientation_returnsOrientation(final String orientation, final Orientation expected) {
        assertThat(Orientation.fromString(orientation)).isEqualTo(expected);
    }

    private static Stream<Arguments> validOrientations() {
        return Stream.of(
                Arguments.of("N", Orientation.N),
                Arguments.of("n", Orientation.N),
                Arguments.of("S", Orientation.S),
                Arguments.of("s", Orientation.S),
                Arguments.of("E", Orientation.E),
                Arguments.of("e", Orientation.E),
                Arguments.of("W", Orientation.W),
                Arguments.of("w", Orientation.W)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "north", "south", "east", "west"})
    void whenInvalidOrientation_throwsUnknownOrientationException(final String orientation) {
        assertThatThrownBy(() -> Orientation.fromString(orientation))
                .isInstanceOf(UnknownOrientationException.class)
                .hasMessage(UnknownOrientationException.MESSAGE.formatted(orientation, "[N, S, E, W]"));
    }

    @ParameterizedTest
    @MethodSource("leftTurns")
    void whenTurnLeft_returnsExpectedOrientation(final Orientation orientation, final Orientation expected) {
        assertThat(orientation.turnLeft()).isEqualTo(expected);
    }

    private static Stream<Arguments> leftTurns() {
        return Stream.of(
                Arguments.of(Orientation.N, Orientation.W),
                Arguments.of(Orientation.S, Orientation.E),
                Arguments.of(Orientation.E, Orientation.N),
                Arguments.of(Orientation.W, Orientation.S)
        );
    }

    @ParameterizedTest
    @MethodSource("rightTurns")
    void whenTurnRight_returnsExpectedOrientation(final Orientation orientation, final Orientation expected) {
        assertThat(orientation.turnRight()).isEqualTo(expected);
    }

    private static Stream<Arguments> rightTurns() {
        return Stream.of(
                Arguments.of(Orientation.N, Orientation.E),
                Arguments.of(Orientation.S, Orientation.W),
                Arguments.of(Orientation.E, Orientation.S),
                Arguments.of(Orientation.W, Orientation.N)
        );
    }

}