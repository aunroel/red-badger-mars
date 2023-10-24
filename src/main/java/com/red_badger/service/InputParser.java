package com.red_badger.service;

import com.red_badger.domain.AbstractRobot;
import com.red_badger.domain.Grid;
import com.red_badger.enums.Command;
import com.red_badger.enums.InputType;
import com.red_badger.enums.Orientation;
import com.red_badger.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputParser {

    private final BufferedReader bufferedReader;
    private InputType inputType;

    public record Configuration(Grid grid, Map<AbstractRobot, List<Command>> robots) {
    }

    public InputParser() {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        inputType = InputType.GRID;
    }

    public Configuration parseInput() {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading input", e);
        }
        validateInput(lines);
        inputType = InputType.GRID;
        return parseConfiguration(lines);
    }

    private Configuration parseConfiguration(final List<String> lines) {
        Grid grid = null;
        AbstractRobot currentRobot = null;

        Map<AbstractRobot, List<Command>> robots = new HashMap<>();


        return new Configuration(grid, robots);
    }

    public void validateInput(final List<String> input) throws InvalidInputException {
        for (String line : input) {
            switch (inputType) {
                case GRID:
                    validateGrid(line);
                    break;
                case ROBOT:
                    validateRobotInput(line);
                    break;
                case INSTRUCTION:
                    validateInstructionInput(line);
                    break;
            }
        }
    }

    private void validateInstructionInput(final String line) {
        if (line.isEmpty()) {
            throw new IllegalArgumentException("Instruction cannot be empty");
        }
        final var validInstructions = Stream.of(Command.values())
                .map(Command::getCommand)
                .collect(Collectors.toSet());

        Stream.of(line.split(""))
                .filter(c -> !validInstructions.contains(c))
                .findFirst()
                .ifPresentOrElse(
                        unknown -> {
                            throw new InvalidInputException("Unknown instruction: %s. Instruction must be one of %s".formatted(unknown, Arrays.toString(Command.values())));
                        },
                        () -> inputType = InputType.ROBOT
                );
    }

    private void validateRobotInput(final String line) {
        final String[] input = line.split(" ");
        if (input.length != 3) {
            throw new InvalidInputException("Robot instruction must have 3 parts, provided = '" + line + "'");
        }

        try {
            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);
            if (x < Grid.MIN_VALUE || y < Grid.MIN_VALUE || x > Grid.MAX_VALUE || y > Grid.MAX_VALUE) {
                throw new InvalidInputException("");
            }

            final String direction = input[2];
            final String validDirections = Stream.of(Orientation.values())
                    .map(Orientation::getOrientation)
                    .collect(Collectors.joining(""));

            if (!direction.matches("[" + validDirections + "]")) {
                throw new InvalidInputException("Invalid robot orientation: %s. Orientation must be one of %s".formatted(direction, Arrays.toString(Orientation.values())));
            }

            inputType = InputType.INSTRUCTION;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Robot position must be a number. " + e.getMessage());
        }
    }

    private void validateGrid(final String line) {
        final String[] input = line.split(" ");
        if (input.length != 2) {
            throw new IllegalArgumentException("Grid size must have 2 parts, provided = '" + line + "'");
        }
        try {
            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);
            if (x < Grid.MIN_VALUE || y < Grid.MIN_VALUE || x > Grid.MAX_VALUE || y > Grid.MAX_VALUE) {
                throw new IllegalArgumentException("Invalid grid coordinates: %d %d. Position must be within ranges %d-%d".formatted(x, y, Grid.MIN_VALUE, Grid.MAX_VALUE));
            }
            inputType = InputType.ROBOT;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grid size must be integer, provided = '" + line + "'");
        }

    }
}
