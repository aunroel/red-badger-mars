package com.red_badger.enums;

import com.red_badger.exception.UnknownCommandException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Command {
    L("L", false, 1),
    R("R", false, 1),
    F("F", true, 1);

    private final String command;
    private final boolean isMovement;
    private final int directionMultiplier;

    public static Command fromString(final String command) {
        return Stream.of(Command.values())
                .filter(c -> c.command.equalsIgnoreCase(command))
                .findFirst()
                .orElseThrow(() -> new UnknownCommandException(command));
    }

    public static Set<Command> getMovementCommands() {
        return Stream.of(Command.values())
                .filter(Command::isMovement)
                .collect(Collectors.toSet());
    }

    public static Set<Command> getStaticCommands() {
        return Stream.of(Command.values())
                .filter(c -> !c.isMovement)
                .collect(Collectors.toSet());
    }
}
