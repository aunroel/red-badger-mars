package com.red_badger.exception;

import com.red_badger.enums.Orientation;

import java.io.Serial;
import java.util.Arrays;

public class UnknownOrientationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7763295158065302894L;

    public static final String MESSAGE = "Unknown orientation: %s. Valid orientations: %s";

    public UnknownOrientationException(final String orientation) {
        super(String.format(MESSAGE, orientation, Arrays.toString(Orientation.values())));
    }
}
