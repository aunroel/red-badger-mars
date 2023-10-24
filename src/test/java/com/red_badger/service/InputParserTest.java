package com.red_badger.service;

import com.red_badger.domain.Grid;
import com.red_badger.domain.MarsRobot;
import com.red_badger.enums.Command;
import com.red_badger.enums.InputType;
import com.red_badger.enums.Orientation;
import com.red_badger.exception.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.red_badger.service.InputParser.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    InputParser classUnderTest;

    @Test
    @DisplayName("Throws if robot position is outside grid")
    void parseConfigurationsThrowsWhenRobotPositionOutsideGrid() {
        classUnderTest = new InputParser();
        assertThatThrownBy(() -> classUnderTest.parseConfiguration(List.of("5 3", "6 1 E", "RFRFRFRF")))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageStartingWith(InvalidInputException.MESSAGE.formatted("Robot position is outside of grid"));
    }

    @Test
    @DisplayName("Returns configuration with grid and robot on valid input")
    void parseConfigurationsReturnsOnValidInput() {
        classUnderTest = new InputParser();
        final Configuration configuration = classUnderTest.parseConfiguration(List.of("5 3", "1 1 E", "RFRFRFRF", "3 2 N", "FRRFLLFFRRFLL", "0 3 W", "LLFFFLFLFL"));

        assertThat(configuration.grid())
                .extracting(Grid::getUpperRightX, Grid::getUpperRightY)
                .containsExactly(5, 3);

        assertThat(configuration.robots()).hasSize(3);
        assertThat(configuration.robots().keySet()).containsExactly(
                new MarsRobot(1, 1, Orientation.E),
                new MarsRobot(3, 2, Orientation.N),
                new MarsRobot(0, 3, Orientation.W)
        );
        assertThat(configuration.robots().values()).containsExactly(
                List.of(Command.R, Command.F, Command.R, Command.F, Command.R, Command.F, Command.R, Command.F),
                List.of(Command.F, Command.R, Command.R, Command.F, Command.L, Command.L, Command.F, Command.F, Command.R, Command.R, Command.F, Command.L, Command.L),
                List.of(Command.L, Command.L, Command.F, Command.F, Command.F, Command.L, Command.F, Command.L, Command.F, Command.L)
        );
    }

    @ParameterizedTest
    @MethodSource("validInput")
    void validateInputDoesNotThrowOnValidInput(final List<String> lines) {
        classUnderTest = new InputParser();
        assertDoesNotThrow(() -> classUnderTest.validateInput(lines));
    }

    private static Stream<Arguments> validInput() {
        return Stream.of(
                Arguments.of(List.of("5 3", "1 1 E", "RFRFRFRF")),
                Arguments.of(List.of("5 3", "3 2 N", "FRRFLLFFRRFLL")),
                Arguments.of(List.of("5 3", "0 3 W", "LLFFFLFLFL")),
                Arguments.of(List.of("5 3", "1 1 E", "RFRFRFRF", "3 2 N", "FRRFLLFFRRFLL", "0 3 W", "LLFFFLFLFL"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInput")
    void validateInputThrowsInvalidInputException(final List<String> lines) {
        classUnderTest = new InputParser();
        assertThatThrownBy(() -> classUnderTest.validateInput(lines))
                .isInstanceOf(InvalidInputException.class);
    }

    private static Stream<Arguments> invalidInput() {
        return Stream.of(
                Arguments.of(List.of()), // too short
                Arguments.of(List.of("1")), // too short
                Arguments.of(List.of("a 1")), // too short
                Arguments.of(List.of("1 1", "1 1")), // too short
                Arguments.of(List.of("1 1", "1 1 1")), // too short
                Arguments.of(List.of("1 d", "0 0 N", "AAA")), // invalid grid
                Arguments.of(List.of("450 7", "0 0 N", "AAA")), // invalid grid
                Arguments.of(List.of("12 1", "0 0 1", "FFF")), // invalid robot
                Arguments.of(List.of("5 3", "1 1 E", "RRRL", "0 1 E", "123")) // invalid instruction
        );
    }

    @Test
    void validateGridDoesNotThrowOnValidInput() {
        classUnderTest = new InputParser();
        assertDoesNotThrow(() -> classUnderTest.validateGridInput("1 1"));
        assertThat(classUnderTest.getInputType()).isEqualTo(InputType.ROBOT);
    }

    @ParameterizedTest
    @MethodSource("invalidGridInput")
    void validateGridThrowsInvalidInputException(final String input, final String expectedMessage) {
        classUnderTest = new InputParser();
        assertThatThrownBy(() -> classUnderTest.validateGridInput(input))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage(InvalidInputException.MESSAGE.formatted(expectedMessage));
    }

    private static Stream<Arguments> invalidGridInput() {
        return Stream.of(
                Arguments.of("", GRID_SIZE_ERROR.formatted("")),
                Arguments.of("1 2 3", GRID_SIZE_ERROR.formatted("1 2 3")),
                Arguments.of("1 a", GRID_SIZE_NUMERIC_ERROR.formatted("1 a")),
                Arguments.of("a 1", GRID_SIZE_NUMERIC_ERROR.formatted("a 1")),
                Arguments.of("-1 1", INVALID_COORDINATES_ERROR.formatted("grid", -1, 1, Grid.MIN_VALUE, Grid.MAX_VALUE)),
                Arguments.of("1 -1", INVALID_COORDINATES_ERROR.formatted("grid", 1, -1, Grid.MIN_VALUE, Grid.MAX_VALUE)),
                Arguments.of("51 1", INVALID_COORDINATES_ERROR.formatted("grid", 51, 1, Grid.MIN_VALUE, Grid.MAX_VALUE)),
                Arguments.of("1 51", INVALID_COORDINATES_ERROR.formatted("grid", 1, 51, Grid.MIN_VALUE, Grid.MAX_VALUE))
        );
    }

    @ParameterizedTest
    @MethodSource("validRobotInput")
    void validateRobotDoesNotThrowOnValidInput(final String input) {
        classUnderTest = new InputParser();
        assertDoesNotThrow(() -> classUnderTest.validateRobotInput(input));
        assertThat(classUnderTest.getInputType()).isEqualTo(InputType.INSTRUCTION);
    }

    private static Stream<Arguments> validRobotInput() {
        return Stream.of(
                Arguments.of("1 1 N"),
                Arguments.of("3 2 E"),
                Arguments.of("0 3 S"),
                Arguments.of("1 1 W")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRobotInput")
    void validateRobotThrowsInvalidInputException(final String input, final String expectedMessage) {
        classUnderTest = new InputParser();
        assertThatThrownBy(() -> classUnderTest.validateRobotInput(input))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageStartingWith(InvalidInputException.MESSAGE.formatted(expectedMessage));
    }

    private static Stream<Arguments> invalidRobotInput() {
        return Stream.of(
                Arguments.of("", ROBOT_INSTRUCTION_SIZE_ERROR.formatted("")),
                Arguments.of("1 2 3 4", ROBOT_INSTRUCTION_SIZE_ERROR.formatted("1 2 3 4")),
                Arguments.of("1 a N", ROBOT_POSITION_NUMBER_ERROR),
                Arguments.of("a 1 N", ROBOT_POSITION_NUMBER_ERROR),
                Arguments.of("1 1 1", ROBOT_ORIENTATION_ERROR.formatted("1", Arrays.toString(Orientation.values()))),
                Arguments.of("-1 1 N", INVALID_COORDINATES_ERROR.formatted("robot", -1, 1, Grid.MIN_VALUE, Grid.MAX_VALUE)),
                Arguments.of("1 -1 N", INVALID_COORDINATES_ERROR.formatted("robot", 1, -1, Grid.MIN_VALUE, Grid.MAX_VALUE)),
                Arguments.of("51 1 N", INVALID_COORDINATES_ERROR.formatted("robot", 51, 1, Grid.MIN_VALUE, Grid.MAX_VALUE)),
                Arguments.of("1 51 N", INVALID_COORDINATES_ERROR.formatted("robot", 1, 51, Grid.MIN_VALUE, Grid.MAX_VALUE))
        );
    }

    @ParameterizedTest
    @MethodSource("validInstructionInput")
    void validateInstructionInputDoesNotThrowOnValidInput(final String input) {
        classUnderTest = new InputParser();
        assertDoesNotThrow(() -> classUnderTest.validateInstructionInput(input));
        assertThat(classUnderTest.getInputType()).isEqualTo(InputType.ROBOT);
    }

    private static Stream<Arguments> validInstructionInput() {
        return Stream.of(
                Arguments.of("L"),
                Arguments.of("R"),
                Arguments.of("LL"),
                Arguments.of("LLFFFLFLFL"),
                Arguments.of("RFRFRFRF"),
                Arguments.of("FRRFLLFFRRFLL")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInstructionInput")
    void validateInstructionInputThrowsInvalidInputException(final String input, final String expectedMessage) {
        classUnderTest = new InputParser();
        assertThatThrownBy(() -> classUnderTest.validateInstructionInput(input))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageStartingWith(InvalidInputException.MESSAGE.formatted(expectedMessage));
    }

    private static Stream<Arguments> invalidInstructionInput() {
        return Stream.of(
                Arguments.of("A", UNKNOWN_INSTRUCTION_ERROR.formatted("A", Arrays.toString(Command.values()))),
                Arguments.of("L R", UNKNOWN_INSTRUCTION_ERROR.formatted(" ", Arrays.toString(Command.values())))
        );
    }

}