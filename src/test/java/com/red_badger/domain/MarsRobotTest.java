package com.red_badger.domain;

import com.red_badger.enums.Command;
import com.red_badger.enums.Orientation;
import com.red_badger.exception.RobotLostException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MarsRobotTest {

    @ParameterizedTest
    @MethodSource("invalidMoveCommands")
    void whenUnsupportedTurnCommand_throwsUnsupportedOperationException(final Command command) {
        final MarsRobot robot = MarsRobot.builder().build();
        assertThatThrownBy(() -> robot.turn(command))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Unsupported turn command: %s".formatted(command));
    }

    private static Stream<Arguments> invalidMoveCommands() {
        return Command.getMovementCommands().stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("validTurnCommands")
    void whenTurnCommand_turnsRobot(final Orientation orientation, final Command command, final Orientation expectedOrientation) {
        final MarsRobot robot = MarsRobot.builder().xCord(0).yCord(0).orientation(orientation).build();
        robot.turn(command);
        assertThat(robot.getOrientation()).isEqualTo(expectedOrientation);
    }

    private static Stream<Arguments> validTurnCommands() {
        return Stream.of(
                Arguments.of(Orientation.S, Command.L, Orientation.E),
                Arguments.of(Orientation.S, Command.R, Orientation.W),
                Arguments.of(Orientation.E, Command.L, Orientation.N),
                Arguments.of(Orientation.E, Command.R, Orientation.S),
                Arguments.of(Orientation.N, Command.L, Orientation.W),
                Arguments.of(Orientation.N, Command.R, Orientation.E),
                Arguments.of(Orientation.W, Command.L, Orientation.S),
                Arguments.of(Orientation.W, Command.R, Orientation.N)
        );
    }

    @Test
    void whenValidMoveCommand_movesRobot() {
        final var robot = MarsRobot.builder().xCord(0).yCord(0).orientation(Orientation.N).build();
        final var grid = spy(Grid.createGrid(5, 3));

        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(0);

        robot.move(grid, Command.F);
        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(1);

        verify(grid).hasPositionBeenScented(0, 0, Orientation.N);
        verify(grid).isValidPosition(0, 1);
        verifyNoMoreInteractions(grid);
    }

    @Test
    void ignoresCommandIfPositionHasBeenScentedForOrientation() {
        final var robot = MarsRobot.builder().xCord(0).yCord(0).orientation(Orientation.W).build();
        final var grid = spy(Grid.createGrid(5, 3));

        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(0);

        grid.scentPosition(0, 0, Orientation.W);

        robot.move(grid, Command.F);
        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(0);

        verify(grid).scentPosition(0, 0, Orientation.W);
        verify(grid).hasPositionBeenScented(0, 0, Orientation.W);
        verifyNoMoreInteractions(grid);
    }

    @Test
    void movesThroughScentedPositionIfOrientationIsDifferent() {
        final var robot = MarsRobot.builder().xCord(0).yCord(0).orientation(Orientation.N).build();
        final var grid = spy(Grid.createGrid(5, 3));

        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(0);

        grid.scentPosition(0, 0, Orientation.W);

        robot.move(grid, Command.F);
        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(1);

        verify(grid).scentPosition(0, 0, Orientation.W);
        verify(grid).hasPositionBeenScented(0, 0, Orientation.N);
        verify(grid).isValidPosition(0, 1);
        verifyNoMoreInteractions(grid);
    }

    @Test
    void throwsScentedExceptionIfMoveCommandIsInvalid() {
        final var robot = MarsRobot.builder().xCord(0).yCord(0).orientation(Orientation.S).build();
        final var grid = spy(Grid.createGrid(5, 3));

        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(0);

        assertThatThrownBy(() -> robot.move(grid, Command.F))
                .isInstanceOf(RobotLostException.class);

        assertThat(robot.getXCord()).isEqualTo(0);
        assertThat(robot.getYCord()).isEqualTo(0);

        verify(grid).hasPositionBeenScented(0, 0, Orientation.S);
        verify(grid).isValidPosition(0, -1);
        verify(grid).scentPosition(0, 0, Orientation.S);
        verifyNoMoreInteractions(grid);
    }

    @Test
    void whenValidCommands_ProcessAll() {
        final var robot = MarsRobot.builder().xCord(1).yCord(1).orientation(Orientation.E).build();
        final var grid = spy(Grid.createGrid(5, 3));

        assertThat(robot.getXCord()).isEqualTo(1);
        assertThat(robot.getYCord()).isEqualTo(1);

        robot.processCommands(grid, List.of(Command.R, Command.F, Command.R, Command.F, Command.R, Command.F, Command.R, Command.F));
        assertThat(robot.getXCord()).isEqualTo(1);
        assertThat(robot.getYCord()).isEqualTo(1);
        assertThat(robot.getOrientation()).isEqualTo(Orientation.E);

        verify(grid, times(4)).hasPositionBeenScented(anyInt(), anyInt(), any(Orientation.class));
        verify(grid, times(4)).isValidPosition(anyInt(), anyInt());
        verifyNoMoreInteractions(grid);
    }

    @Test
    void whenOutOfGridCommand_throwRobotLostException() {
        final var robot = MarsRobot.builder().xCord(3).yCord(2).orientation(Orientation.N).build();
        final var grid = spy(Grid.createGrid(5, 3));

        assertThat(robot.getXCord()).isEqualTo(3);
        assertThat(robot.getYCord()).isEqualTo(2);

        final var commands = List.of(
                Command.F,
                Command.R,
                Command.R,
                Command.F,
                Command.L,
                Command.L,
                Command.F,
                Command.F,
                Command.R,
                Command.R,
                Command.F,
                Command.L,
                Command.L
        );

        assertThatThrownBy(() -> robot.processCommands(grid, commands))
                .isInstanceOf(RobotLostException.class);
        assertThat(robot.getXCord()).isEqualTo(3);
        assertThat(robot.getYCord()).isEqualTo(3);
        assertThat(robot.getOrientation()).isEqualTo(Orientation.N);
        assertThat(grid.hasPositionBeenScented(3, 3, Orientation.N)).isTrue();

        // 5 and not 4 because we called 'hasPositionBeenScented' in the previous assertion
        verify(grid, times(5)).hasPositionBeenScented(anyInt(), anyInt(), any(Orientation.class));
        verify(grid, times(4)).isValidPosition(anyInt(), anyInt());
        verify(grid).scentPosition(3, 3, Orientation.N);
        verifyNoMoreInteractions(grid);
    }

}