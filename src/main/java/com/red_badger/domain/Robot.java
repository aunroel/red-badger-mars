package com.red_badger.domain;

import com.red_badger.enums.Orientation;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@Builder(toBuilder = true)
public class Robot {
    private int xCord;
    private int yCord;
    private Orientation orientation;
}
