package com.red_badger.enums;

import com.red_badger.exception.UnknownCommandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandTest {

    @ParameterizedTest
    @MethodSource("validCommands")
    void whenValidCommand_returnsCommand(final String command, final Command expected) {
        assertThat(Command.fromString(command)).isEqualTo(expected);
    }

    private static Stream<Arguments> validCommands() {
        return Stream.of(
                Arguments.of("L", Command.L),
                Arguments.of("l", Command.L),
                Arguments.of("R", Command.R),
                Arguments.of("r", Command.R),
                Arguments.of("F", Command.F),
                Arguments.of("f", Command.F)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "left", "right", "forward"})
    void whenInvalidCommand_throwsUnknownCommandException(final String command) {
        assertThatThrownBy(() -> Command.fromString(command))
                .isInstanceOf(UnknownCommandException.class)
                .hasMessage(UnknownCommandException.MESSAGE.formatted(command, "[L, R, F]"));
    }

    @Test
    void whenGetMovementCommands_returnsMovementCommands() {
        assertThat(Command.getMovementCommands()).containsExactlyInAnyOrder(Command.F);
    }

    @Test
    void whenGetStaticCommands_returnsStaticCommands() {
        assertThat(Command.getStaticCommands()).containsExactlyInAnyOrder(Command.L, Command.R);
    }

}