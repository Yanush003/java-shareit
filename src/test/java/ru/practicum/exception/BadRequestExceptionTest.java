package ru.practicum.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadRequestExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Bad request occurred";

        Exception exception = assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
