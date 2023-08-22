package ru.practicum.booking;

import java.util.Optional;

public enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    FUTURE,
    PAST,
    CURRENT,
    ALL,
    CANCELED;

    public static Optional<Status> from(String stringState) {
        for (Status state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
