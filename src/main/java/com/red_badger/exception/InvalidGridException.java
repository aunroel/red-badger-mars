package com.red_badger.exception;

import com.red_badger.domain.Grid;

public class InvalidGridException extends RuntimeException {

    private static final String MESSAGE = "Invalid grid: [%d, %d]. Valid grid: %d <= x, y <= %d";

    public InvalidGridException(final int xCord, final int yCord) {
        super(MESSAGE.formatted(xCord, yCord, Grid.MIN_VALUE, Grid.MAX_VALUE));
    }
}
