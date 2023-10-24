package com.red_badger;

import com.red_badger.exception.InvalidInputException;
import com.red_badger.service.InputProcessor;
import com.red_badger.service.InputParser;

public class MartianRobotsApplication {

    public static void main(String[] args) {
        final var validator = new InputParser();
        try {
            final InputParser.Configuration configuration = validator.parseInput();
            final var inputProcessor = new InputProcessor(configuration.grid(), configuration.robots());
            inputProcessor.processInput().forEach(System.out::println);
        } catch (final InvalidInputException e) {
            System.err.println(e.getMessage());
        } catch (final Exception e) {
            System.err.println("Something went wrong. Please try again.");
        }
    }
}
