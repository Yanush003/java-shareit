package ru.practicum.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotFoundExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Resource not found";

        Exception exception = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
