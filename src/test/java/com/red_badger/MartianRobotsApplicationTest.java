package com.red_badger;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;
import static org.assertj.core.api.Assertions.assertThat;

class MartianRobotsApplicationTest {

    @Test
    @SneakyThrows
    void correctOutputReturnedOnValidInput() {
        final var input = """
                5 3
                1 1 E
                RFRFRFRF
                
                3 2 N
                FRRFLLFFRRFLL
                
                0 3 W
                LLFFFLFLFL
                """;

        final var expected = """
                1 1 E
                3 3 N LOST
                2 3 S
                """;
        withTextFromSystemIn(input)
                .execute(() -> {
                    final var actual = tapSystemOutNormalized(() -> MartianRobotsApplication.main(new String[]{}));
                    assertThat(actual).isEqualTo(expected);
                });
    }

    @Test
    @SneakyThrows
    @DisplayName("Returns correct output when input has empty lines, whitespaces (line start / end) and case-insensitive commands / orientations")
    void whitespacesAndEmptyLinesIgnored() {
        final var input = """
                5 3
                      1 1 E
                RFrfRFRF            
                
                3 2 n
                FRRFLLFFRRFLL
                
                                0 3 w
                LLFFFLFLFL
                """;

        final var expected = """
                1 1 E
                3 3 N LOST
                2 3 S
                """;
        withTextFromSystemIn(input)
                .execute(() -> {
                    final var actual = tapSystemOutNormalized(() -> MartianRobotsApplication.main(new String[]{}));
                    assertThat(actual).isEqualTo(expected);
                });
    }

    @Test
    @SneakyThrows
    void errorMessageDisplayedOnInvalidInput() {
        final var input = """
                5 3
                0 0
                RFRFRFRF
                """;

        final var expected = """
               Invalid input: Robot instruction must have 3 parts, provided = '0 0'
                """;
        withTextFromSystemIn(input)
                .execute(() -> {
                    final var actual = tapSystemErrNormalized(() -> MartianRobotsApplication.main(new String[]{}));
                    assertThat(actual).isEqualTo(expected);
                });
    }
}