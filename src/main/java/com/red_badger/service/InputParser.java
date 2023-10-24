package com.red_badger.service;

import com.red_badger.domain.AbstractRobot;
import com.red_badger.domain.Grid;
import com.red_badger.domain.MarsRobot;
import com.red_badger.enums.Command;
import com.red_badger.enums.InputType;
import com.red_badger.enums.Orientation;
import com.red_badger.exception.InvalidInputException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputParser {

    public static final String GRID_SIZE_NUMERIC_ERROR = "Grid size must be numeric, provided = '%s'";
    public static final String INVALID_COORDINATES_ERROR = "Invalid %s coordinates: %d %d. Position must be within ranges %d-%d";
    public static final String GRID_SIZE_ERROR = "Grid size must have 2 parts, provided = '%s'";
    public static final String ROBOT_POSITION_NUMBER_ERROR = "Robot position must be a number. ";
    public static final String ROBOT_ORIENTATION_ERROR = "Invalid robot orientation: %s. Orientation must be one of %s";
    public static final String ROBOT_INSTRUCTION_SIZE_ERROR = "Robot instruction must have 3 parts, provided = '%s'";
    public static final String MINIMAL_INSTRUCTION_ERROR = "Provide grid size, robot position and instructions";
    public static final String UNKNOWN_INSTRUCTION_ERROR = "Unknown instruction: '%s'. Instruction must be one of %s";
    public static final String INSTRUCTION_SIZE_ERROR = "Instructions must be less than %d characters, provided - '%d' (%s)";
    public static final int MAX_INSTRUCTION_SIZE = 100;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private InputType inputType;

    public record Configuration(Grid grid, Map<AbstractRobot, List<Command>> robots) {
    }

    public InputParser() {
        inputType = InputType.GRID;
    }

    public Configuration parseInput() {
        final List<String> lines = new ArrayList<>();
        String line;

        try (var bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                lines.add(line.trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading input", e);
        }

        validateInput(lines);
        return parseConfiguration(lines);
    }

    protected Configuration parseConfiguration(final List<String> lines) {
        Grid grid = null;
        AbstractRobot currentRobot = null;
        Map<AbstractRobot, List<Command>> robots = new LinkedHashMap<>();

        inputType = InputType.GRID;
        for (String line : lines) {
            switch (inputType) {
                case GRID:
                    final String[] gridInput = line.split(" ");
                    grid = Grid.createGrid(Integer.parseInt(gridInput[0]), Integer.parseInt(gridInput[1]));
                    inputType = InputType.ROBOT;
                    break;
                case ROBOT:
                    final String[] robotInput = line.split(" ");
                    final int xCord = Integer.parseInt(robotInput[0]);
                    final int yCord = Integer.parseInt(robotInput[1]);

                    // safe to ignore warning as grid is always initialised before robot
                    if (!grid.isValidPosition(xCord, yCord)) {
                        throw new InvalidInputException(
                                "Robot position is outside of grid: %d %d. Position must be within ranges %d-%d"
                                        .formatted(xCord, yCord, grid.getUpperRightX(), grid.getUpperRightY())
                        );
                    }
                    final Orientation orientation = Orientation.fromString(robotInput[2]);
                    currentRobot = new MarsRobot(xCord, yCord, orientation);
                    robots.put(currentRobot, new ArrayList<>());
                    inputType = InputType.INSTRUCTION;
                    break;
                case INSTRUCTION:
                    final AbstractRobot finalCurrentRobot = currentRobot;
                    Stream.of(line.split(""))
                            .map(Command::fromString)
                            .forEach(command -> robots.get(finalCurrentRobot).add(command));
                    inputType = InputType.ROBOT;
                    currentRobot = null;
                    break;
            }
        }

        return new Configuration(grid, robots);
    }

    protected void validateInput(final List<String> input) throws InvalidInputException {
        if (input.size() < 3) {
            throw new InvalidInputException(MINIMAL_INSTRUCTION_ERROR);
        }
        for (String line : input) {
            switch (inputType) {
                case GRID:
                    validateGridInput(line);
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

    protected void validateInstructionInput(final String line) {
        if (line.length() > MAX_INSTRUCTION_SIZE) {
            throw new InvalidInputException(INSTRUCTION_SIZE_ERROR.formatted(MAX_INSTRUCTION_SIZE, line.length(), line));
        }
        final var validInstructions = Stream.of(Command.values())
                .map(Command::getCommand)
                .collect(Collectors.toSet());

        Stream.of(line.split(""))
                .filter(c -> !validInstructions.contains(c.toUpperCase(Locale.ROOT)))
                .findFirst()
                .ifPresentOrElse(
                        unknown -> {
                            throw new InvalidInputException(UNKNOWN_INSTRUCTION_ERROR.formatted(unknown, Arrays.toString(Command.values())));
                        },
                        () -> inputType = InputType.ROBOT
                );
    }

    protected void validateRobotInput(final String line) {
        final String[] input = line.split(" ");
        if (input.length != 3) {
            throw new InvalidInputException(ROBOT_INSTRUCTION_SIZE_ERROR.formatted(line));
        }

        try {
            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);
            validateInputPositionRange(x, y, "robot");

            final String direction = input[2].toUpperCase(Locale.ROOT);
            final String validDirections = Stream.of(Orientation.values())
                    .map(Orientation::getOrientation)
                    .collect(Collectors.joining(""));

            if (!direction.matches("[" + validDirections + "]")) {
                throw new InvalidInputException(ROBOT_ORIENTATION_ERROR.formatted(direction, Arrays.toString(Orientation.values())));
            }

            inputType = InputType.INSTRUCTION;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(ROBOT_POSITION_NUMBER_ERROR + e.getMessage());
        }
    }

    private static void validateInputPositionRange(int x, int y, String type) {
        if (x < Grid.MIN_VALUE || y < Grid.MIN_VALUE || x > Grid.MAX_VALUE || y > Grid.MAX_VALUE) {
            throw new InvalidInputException(INVALID_COORDINATES_ERROR.formatted(type, x, y, Grid.MIN_VALUE, Grid.MAX_VALUE));
        }
    }

    protected void validateGridInput(final String line) {
        final String[] input = line.split(" ");
        if (input.length != 2) {
            throw new InvalidInputException(GRID_SIZE_ERROR.formatted(line));
        }
        try {
            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);
            validateInputPositionRange(x, y, "grid");

            inputType = InputType.ROBOT;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(GRID_SIZE_NUMERIC_ERROR.formatted(line));
        }

    }
}
