package ru.practicum.shareit.exception;

public class BadRequest400 extends RuntimeException {
    public BadRequest400(String message) {
        super(message);
    }
}
