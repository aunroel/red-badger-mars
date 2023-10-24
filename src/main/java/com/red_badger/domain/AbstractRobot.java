package com.red_badger.domain;

import com.red_badger.enums.Orientation;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractRobot {
    private int xCord;
    private int yCord;
    private Orientation orientation;
}
