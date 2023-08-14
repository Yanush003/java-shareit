package ru.practicum.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class StatusTest {
    @Test
    void fromTest(){
        Optional<Status> actualOptionalStatus = Status.from("FUTURE");
        Assertions.assertTrue(actualOptionalStatus.isPresent());
        Assertions.assertEquals(Status.FUTURE, actualOptionalStatus.get());

        Optional<Status> actualEmptyOptionalStatus = Status.from("FE");
        Assertions.assertFalse(actualEmptyOptionalStatus.isPresent());
    }
}
