package com.red_badger.exception;

import com.red_badger.domain.Grid;

import java.io.Serial;

public class InvalidGridException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2253742203239077550L;

    public static final String MESSAGE = "Invalid grid: [%d, %d]. Valid grid: %d <= x, y <= %d";

    public InvalidGridException(final int xCord, final int yCord) {
        super(MESSAGE.formatted(xCord, yCord, Grid.MIN_VALUE, Grid.MAX_VALUE));
    }
}
