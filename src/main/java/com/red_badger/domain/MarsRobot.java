package com.red_badger.domain;

import com.red_badger.enums.Command;
import com.red_badger.enums.Orientation;
import com.red_badger.exception.RobotLostException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MarsRobot extends AbstractRobot {

    public MarsRobot(final int xCord, final int yCord, final Orientation orientation) {
        super(xCord, yCord, orientation);
    }

    @Override
    public void turn(final Command command) {
        switch (command) {
            case L -> super.setOrientation(super.getOrientation().turnLeft());
            case R -> super.setOrientation(super.getOrientation().turnRight());
            default -> throw new UnsupportedOperationException("Unsupported turn command: " + command);
        }
    }

    @Override
    public void move(final Grid grid, final Command command) throws RobotLostException {
        final int oldXCord = super.getXCord();
        final int oldYCord = super.getYCord();

        if (grid.hasPositionBeenScented(oldXCord, oldYCord, super.getOrientation())) {
            return;
        }

        final int newXCord = oldXCord + (command.getDirectionMultiplier() * super.getOrientation().getXIncrement());
        final int newYCord = oldYCord + (command.getDirectionMultiplier() * super.getOrientation().getYIncrement());

        if (grid.isValidPosition(newXCord, newYCord)) {
            super.setXCord(newXCord);
            super.setYCord(newYCord);
        } else {
            grid.scentPosition(oldXCord, oldYCord, super.getOrientation());
            throw new RobotLostException();
        }
    }
}
