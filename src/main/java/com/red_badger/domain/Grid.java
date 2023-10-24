package com.red_badger.domain;

import com.red_badger.enums.Orientation;
import com.red_badger.exception.InvalidGridException;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Grid {

    public static final int MAX_VALUE = 50;
    public static final int MIN_VALUE = 0;

    private record GridPosition(int xCord, int yCord) {}

    int upperRightX;
    int upperRightY;

    Map<GridPosition, Set<Orientation>> scentedPositions;

    private Grid(int upperRightX, int upperRightY) {
        this.upperRightX = upperRightX;
        this.upperRightY = upperRightY;
        this.scentedPositions = new HashMap<>();
    }

    public static Grid createGrid(final int upperRightX, final int upperRightY) {
        validateGrid(upperRightX, upperRightY);
        return new Grid(upperRightX, upperRightY);
    }

    private static void validateGrid(final int xCord, final int yCord) {
        if (xCord < MIN_VALUE || xCord > MAX_VALUE || yCord < MIN_VALUE || yCord > MAX_VALUE) {
            throw new InvalidGridException(xCord, yCord);
        }
    }

    public boolean isPositionWithinBounds(final int xCord, final int yCord) {
        return xCord >= MIN_VALUE && xCord <= upperRightX && yCord >= MIN_VALUE && yCord <= upperRightY;
    }

    public boolean hasPositionBeenScented(final int xCord, final int yCord, final Orientation orientation) {
        return Optional.ofNullable(scentedPositions.get(new GridPosition(xCord, yCord)))
                .map(orientations -> orientations.contains(orientation))
                .orElse(false);
    }

    public void scentPosition(final int xCord, final int yCord, final Orientation orientation) {
        final var gridPosition = new GridPosition(xCord, yCord);
        Optional.ofNullable(scentedPositions.get(gridPosition))
                .ifPresentOrElse(
                        orientations -> orientations.add(orientation),
                        () -> scentedPositions.put(gridPosition, Set.of(orientation))
                );
    }
}
