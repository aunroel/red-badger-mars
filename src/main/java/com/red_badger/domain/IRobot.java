package com.red_badger.domain;

import com.red_badger.enums.Command;
import com.red_badger.exception.RobotLostException;

import java.util.List;

public interface IRobot {

    default void processCommands(final Grid grid, final List<Command> commands) {
        commands.forEach(command -> processCommand(grid, command));
    }

    default void processCommand(final Grid grid, final Command command) {
        if (command.isMovement()) {
            move(grid, command);
        } else {
            turn(command);
        }
    }

    void turn(Command command);

    void move(Grid grid, Command command) throws RobotLostException;

}
