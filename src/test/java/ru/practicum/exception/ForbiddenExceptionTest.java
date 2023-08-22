package ru.practicum.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ForbiddenExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Access is forbidden";

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            throw new ForbiddenException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
