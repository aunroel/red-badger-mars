package com.red_badger.domain;

import com.red_badger.enums.Command;
import com.red_badger.enums.Orientation;
import com.red_badger.exception.RobotLostException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractRobot {

    @Setter(AccessLevel.PROTECTED)
    private int xCord;
    @Setter(AccessLevel.PROTECTED)
    private int yCord;
    @Setter(AccessLevel.PROTECTED)
    private Orientation orientation;

    public AbstractRobot( final int xCord, final int yCord, final Orientation orientation) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.orientation = orientation;
    }

    public void processCommands(final Grid grid, final List<Command> commands) {
        commands.forEach(command -> processCommand(grid, command));
    }

    public void processCommand(final Grid grid, final Command command) {
        if (command.isMovement()) {
            move(grid, command);
        } else {
            turn(command);
        }
    }

    protected abstract void turn(Command command);

    protected abstract void move(Grid grid, Command command) throws RobotLostException;
}
