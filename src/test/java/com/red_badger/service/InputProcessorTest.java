package com.red_badger.service;

import com.red_badger.domain.AbstractRobot;
import com.red_badger.domain.Grid;
import com.red_badger.domain.MarsRobot;
import com.red_badger.enums.Command;
import com.red_badger.enums.Orientation;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.red_badger.enums.Command.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class InputProcessorTest {

    @Test
    void processInputReturnsValidOutput() {
        // given
        var grid = Grid.createGrid(5, 3);
        Map<AbstractRobot, List<Command>> robots = new LinkedHashMap<>();
        robots.put(new MarsRobot(1, 1, Orientation.E), List.of(R,F,R,F,R,F,R,F));
        robots.put(new MarsRobot(3, 2, Orientation.N), List.of(F,R,R,F,L,L,F,F,R,R,F,L,L));
        robots.put(new MarsRobot(0, 3, Orientation.W), List.of(L,L,F,F,F,L,F,L,F,L));

        // when
        InputProcessor classUnderTest = new InputProcessor(grid, robots);
        final List<String> actual = classUnderTest.processInput();

        // then
        List<String> expectedOutput = List.of(
                "1 1 E",
                "3 3 N LOST",
                "2 3 S"
        );
        assertThat(actual)
                .hasSize(3)
                .containsExactlyElementsOf(expectedOutput);
    }

}