package com.red_badger;

import com.red_badger.exception.InvalidInputException;
import com.red_badger.service.InputProcessor;
import com.red_badger.service.InputParser;

public class MartianRobotsApplication {

    public static void main(String[] args) {
        InputParser validator = new InputParser();
        try {
            final InputParser.Configuration configuration = validator.parseInput();
            InputProcessor inputProcessor = new InputProcessor(configuration.grid(), configuration.robots());
            inputProcessor.processInput().forEach(System.out::println);
        } catch (final InvalidInputException e) {
            System.err.println(e.getMessage());

        }
    }
}
