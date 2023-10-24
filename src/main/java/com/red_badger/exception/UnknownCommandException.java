package com.red_badger.exception;

import com.red_badger.enums.Command;

public class UnknownCommandException extends RuntimeException {

    private static final String MESSAGE = "Unknown command: %s. Valid commands: %s";

    public UnknownCommandException(String command) {
        super(MESSAGE.formatted(command, Command.values()));
    }
}
