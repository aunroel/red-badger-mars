package com.red_badger.service;

import com.red_badger.enums.InputType;
import com.red_badger.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    InputParser classUnderTest;

    @Test
    void parseInput() {
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
                .hasMessageStartingWith(InvalidInputException.MESSAGE.formatted(expectedMessage));
    }

    private static Stream<Arguments> invalidGridInput() {
        return Stream.of(
                Arguments.of("", "Grid size must have 2 parts"),
                Arguments.of("1 2 3", "Grid size must have 2 parts"),
                Arguments.of("1 a", "Grid size must be numeric"),
                Arguments.of("a 1", "Grid size must be numeric"),
                Arguments.of("-1 1", "Invalid grid coordinates"),
                Arguments.of("1 -1", "Invalid grid coordinates"),
                Arguments.of("51 1", "Invalid grid coordinates"),
                Arguments.of("1 51", "Invalid grid coordinates")
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
                Arguments.of("", "Robot instruction must have 3 parts"),
                Arguments.of("1 2 3 4", "Robot instruction must have 3 parts"),
                Arguments.of("1 a N", "Robot position must be a number"),
                Arguments.of("a 1 N", "Robot position must be a number"),
                Arguments.of("1 1 1", "Invalid robot orientation: 1."),
                Arguments.of("-1 1 N", "Invalid robot coordinates: -1 1."),
                Arguments.of("1 -1 N", "Invalid robot coordinates: 1 -1."),
                Arguments.of("51 1 N", "Invalid robot coordinates: 51 1."),
                Arguments.of("1 51 N", "Invalid robot coordinates: 1 51.")
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
                Arguments.of("", "Instruction cannot be empty"),
                Arguments.of("A", "Unknown instruction: 'A'."),
                Arguments.of("L R", "Unknown instruction: ' '.")
        );
    }

}