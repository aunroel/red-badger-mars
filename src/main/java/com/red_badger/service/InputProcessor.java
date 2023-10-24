package com.red_badger.service;

import com.red_badger.domain.AbstractRobot;
import com.red_badger.domain.Grid;
import com.red_badger.enums.Command;
import com.red_badger.exception.RobotLostException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InputProcessor {

    private final Grid grid;
    private final Map<AbstractRobot, List<Command>> robots;

    public List<String> processInput() {
        return robots.keySet().stream()
                .map(robot -> processRobotCommand(robot, robots.get(robot)))
                .collect(Collectors.toList());
    }

    private String processRobotCommand(final AbstractRobot robot, final List<Command> commands) {
        try {
            robot.processCommands(grid, commands);
            return robot.getXCord() + " " + robot.getYCord() + " " + robot.getOrientation().getOrientation();
        } catch (final RobotLostException e) {
            return robot.getXCord() + " " + robot.getYCord() + " " + robot.getOrientation().getOrientation() + " LOST";
        }
    }

}
