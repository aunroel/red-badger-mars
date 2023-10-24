package com.red_badger.exception;

import com.red_badger.enums.Command;

import java.io.Serial;
import java.util.Arrays;

public class UnknownCommandException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1355756615559656167L;

    public static final String MESSAGE = "Unknown command: %s. Valid commands: %s";

    public UnknownCommandException(String command) {
        super(MESSAGE.formatted(command, Arrays.toString(Command.values())));
    }
}
