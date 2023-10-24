package com.red_badger.domain;

import com.red_badger.enums.Orientation;
import com.red_badger.exception.InvalidGridException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class GridTest {

    @ParameterizedTest
    @CsvSource({
        "-1,0",
        "0,-1",
        "51,1",
        "1,51",
    })
    void whenInvalidGridSize_throwsInvalidGridException(final int x, final int y) {
        assertThatThrownBy(() -> Grid.createGrid(x, y))
                .isInstanceOf(InvalidGridException.class)
                .hasMessage(InvalidGridException.MESSAGE.formatted(x, y, Grid.MIN_VALUE, Grid.MAX_VALUE));
    }

    @Test
    void whenValidGridSize_createsGrid() {
        final var grid = Grid.createGrid(1, 1);
        assertThat(grid).isNotNull();
        assertThat(grid.isValidPosition(0, 0)).isTrue();
        assertThat(grid.isValidPosition(1, 1)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,1",
            "3,2"
    })
    void whenValidPosition_returnsTrue(final int x, final int y) {
        final var grid = Grid.createGrid(5, 5);
        assertThat(grid.isValidPosition(x, y)).isTrue();
    }


    @ParameterizedTest
    @CsvSource({
            "-1,0",
            "7,4",
            "6,1"
    })
    void whenInvalidPosition_returnsFalse(final int x, final int y) {
        final var grid = Grid.createGrid(5, 5);
        assertThat(grid.isValidPosition(x, y)).isFalse();
    }

    @Test
    void whenPositionNotScented_returnsFalse() {
        final var grid = Grid.createGrid(5, 5);
        assertThat(grid.hasPositionBeenScented(1, 1, Orientation.E)).isFalse();
    }

    @Test
    void whenPositionScented_returnsTrue() {
        final var grid = Grid.createGrid(5, 5);
        grid.scentPosition(1, 1, Orientation.E);
        assertThat(grid.hasPositionBeenScented(1, 1, Orientation.E)).isTrue();
    }

    @Test
    void whenPositionScentedWithDifferentOrientation_returnsFalse() {
        final var grid = Grid.createGrid(5, 5);
        grid.scentPosition(1, 1, Orientation.E);
        assertThat(grid.hasPositionBeenScented(1, 1, Orientation.W)).isFalse();
    }
}