package com.red_badger.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Orientation {
    N("north", 0, 1),
    S("south", 0, -1),
    E("east", 1, 0),
    W("west", -1, 0);

    private final String orientation;
    private final int xIncrement;
    private final int yIncrement;
}
