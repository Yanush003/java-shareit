package ru.practicum.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WrongDataExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Provided data is incorrect";

        Exception exception = assertThrows(WrongDataException.class, () -> {
            throw new WrongDataException(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
