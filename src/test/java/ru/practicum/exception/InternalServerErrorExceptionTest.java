package ru.practicum.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InternalServerErrorExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Internal server error occurred";

        Exception exception = assertThrows(InternalServerErrorException.class, () -> {
            throw new InternalServerErrorException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
