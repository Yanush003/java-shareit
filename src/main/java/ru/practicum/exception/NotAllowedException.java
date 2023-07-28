package ru.practicum.exception;

public class NotAllowedException extends RuntimeException{
    public NotAllowedException (String message) {
        super(message);
    }
}
