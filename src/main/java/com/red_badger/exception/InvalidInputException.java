package com.red_badger.exception;

import java.io.Serial;

public class InvalidInputException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3217228383840424719L;

    public static final String MESSAGE = "Invalid input: %s";

    public InvalidInputException(final String inputLine) {
        super(MESSAGE.formatted(inputLine));
    }
}
