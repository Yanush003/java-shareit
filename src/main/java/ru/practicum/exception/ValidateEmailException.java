package ru.practicum.exception;

public class ValidateEmailException extends RuntimeException {
    public ValidateEmailException(String message) {
        super(message);
    }
}
