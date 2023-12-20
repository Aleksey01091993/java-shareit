package ru.practicum.shareit.exception;

public class NotFound404 extends RuntimeException {
    public NotFound404(String message) {
        super(message);
    }
}
